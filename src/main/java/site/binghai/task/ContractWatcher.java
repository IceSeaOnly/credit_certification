package site.binghai.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import site.binghai.Enums.ContractStatusEnum;
import site.binghai.config.IceConfig;
import site.binghai.entity.Contract;
import site.binghai.service.ContractService;
import site.binghai.utils.BaseBean;
import site.binghai.utils.HttpUtils;
import site.binghai.utils.TimeTools;

import java.math.BigInteger;
import java.util.List;


/**
 * Created by IceSea on 2018/5/7.
 * GitHub: https://github.com/IceSeaOnly
 */
@Component
@EnableScheduling
public class ContractWatcher extends BaseBean {
    //https://mainnet.nebulas.io
    private static final String url = "/v1/user/getTransactionReceipt";
    private static final String watchContract = "/v1/user/call";

    @Autowired
    private IceConfig iceConfig;
    @Autowired
    private ContractService contractService;

    public void checkTxSuccess(ContractStatusEnum status) {
        List<Contract> contractList = contractService.findByState(status);
        contractList.forEach(v -> {
            JSONObject obj = newJSONObject();
            obj.put("from", v.getFrom());
            obj.put("to", v.getSmartContractAddrsss());
            obj.put("value", "0");
            obj.put("nonce", 0);
            obj.put("gasPrice", "1000000");
            obj.put("gasLimit", "2000000");

            JSONObject c = newJSONObject();
            String args = String.format("[\"%s\"]", v.getUuid());
            c.put("function", "watchContract");
            c.put("args", args);

            obj.put("contract", c);

            try {
                JSONObject res = HttpUtils.sendJSONPost(iceConfig.getServer() + watchContract, null, obj.toJSONString());
                if (res == null) {
                    logger.error("contract {} not found!", v);
                } else {
                    if (status == ContractStatusEnum.USER_CONFIRMED) {
                        checkResult(v, res.getJSONObject("result"));
                    } else {
                        checkComplete(v);
                    }
                }
            } catch (Exception e) {
                logger.error("{} check failed!", v, e);
            }
        });
    }

    @Scheduled(cron = "0 * * * * ?")
    public void task() {
        checkTxSuccess(ContractStatusEnum.USER_CONFIRMED);
        checkTxSuccess(ContractStatusEnum.SYSTEM_CONFRIMED);
    }

    private void checkComplete(Contract contract) {
        if (contract.getEndTimeTs() <= TimeTools.currentTS()) {
            contract.setSysRemark("合约已完成，请手动解约");
            contract.setState(ContractStatusEnum.COMPLETED.getCode());
        } else {
            contract.setSysRemark(String.format("距离合约完成还有 %d 小时", (contract.getEndTimeTs() - TimeTools.currentTS()) / 3600000));
        }
        contractService.update(contract);
    }

    private void checkResult(Contract contract, JSONObject res) {
        String error = res.getString("error");
        if (!StringUtils.isEmpty(error)) {
            logger.error("contract {} check failed! {}", contract, error);
        }
        logger.info("contract check result:{}", res);

        JSONObject innerContract = res.getJSONObject("result");
        if(innerContract == null) return;
        if (!innerContract.getString("val").equals(contract.getVal() + "000000000000000000")) {
            contract.setSysRemark(String.format("交易额与约定不符! %s != %s", innerContract.getString("val"), contract.getVal()+"000000000000000000"));
            contractService.update(contract);
            return;
        }

        if (!contract.getEndTimeTs().equals(innerContract.getLong("endTimeTs"))) {
            contract.setSysRemark(String.format("合约结束时间与约定不符! %d != %d", innerContract.getLong("endTimeTs"), contract.getEndTimeTs()));
            contractService.update(contract);
            return;
        }

        contract.setSysRemark("");
        contract.setState(ContractStatusEnum.SYSTEM_CONFRIMED.getCode());

        contractService.update(contract);
        logger.info("contract pay check success! uuid:{}", contract.getUuid());
    }
}
