package site.binghai.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.binghai.Enums.ContractStatusEnum;
import site.binghai.entity.Contract;
import site.binghai.entity.CreditOffice;
import site.binghai.service.ContractService;
import site.binghai.service.CreditOfficeService;
import site.binghai.utils.TimeTools;

import java.util.List;
import java.util.Map;

/**
 * Created by IceSea on 2018/5/7.
 * GitHub: https://github.com/IceSeaOnly
 */
@RestController
@RequestMapping("/user/")
public class UserController extends BaseController {
    @Autowired
    private CreditOfficeService officeService;
    @Autowired
    private ContractService contractService;

    @GetMapping("listOffice")
    public Object listOffice() {
        JSONArray obj = new JSONArray();
        for (CreditOffice office : officeService.findAll(9999)) {
            JSONObject item = newJSONObject();
            item.put("label", office.getName());
            item.put("value", office.getId());

            obj.add(item);
        }

        return success(obj);
    }

    @GetMapping("listMyContract")
    public Object listMyContract(Long id) {
        if (id != null) {
            Contract contract = contractService.findById(id);
            if (contract == null || !contract.getUserId().equals(getUser().getId())) {
                return fail("无权访问!");
            }
            return success(contractService.toJSONObject(contract));
        }
        List<Contract> contracts = contractService.findByUserId(getUser().getId());
        JSONArray array = newJSONArray();
        contracts.forEach(v -> array.add(contractService.toJSONObject(v)));

        return success(array);
    }

    /**
     * 申请合约
     */
    private int addLen = "n1Gz4vkuCuZCWdmhLMAMzC2GDBYCQZrrdYX".length();

    @PostMapping("applyContract")
    public Object applyContract(@RequestBody Map map) {
        Long officeId = getLong(map, "officeId");
        String from = getString(map,"from");
        CreditOffice office = officeService.findById(officeId);
        if(!noEmptyString(from)){
            return fail("地址必填!");
        }
        if(from.length() != addLen){
            return fail("地址不规范!");
        }
        if (office == null) {
            return fail("机构不存在!");
        }

        Contract contract = new Contract();
        contract.setUserId(getUser().getId());
        contract.setOfficeId(officeId);
        contract.setUserName(getUser().getName());
        contract.setOfficeName(office.getName());
        contract.setSmartContractAddrsss(office.getSmartContractAddrsss());
        contract.setFrom(from);
        contract.setState(ContractStatusEnum.CREATED.getCode());

        contract = contractService.save(contract);
        return success(contract);
    }

    /**
     * 补充合约内容
     */
    @PostMapping("detailContract")
    public Object detailContract(@RequestBody Map map) {
        Long id = getLong(map, "id");
        Contract contract = contractService.findById(id);
        if (contract == null || !contract.getUserId().equals(getUser().getId())) {
            return fail("非法权限!");
        }

        if (ContractStatusEnum.valueOf(contract.getState()) != ContractStatusEnum.CREATED) {
            return fail("合约状态错误!");
        }

        String remark = getString(map, "remark");
        Long val = getLong(map, "val");
        Long endTimeTs = getLong(map, "endTimeTs");

        if (!noEmptyString(remark)) {
            return fail("信息输入不完整!");
        }

        if (val == null || val < 0) {
            return fail("金额必须为不小于0的整数!");
        }

        if (endTimeTs == null || endTimeTs < TimeTools.getTimesnight()) {
            return fail("合约到期时间必须大约今天!");
        }

        contract.setState(ContractStatusEnum.DETAILED.getCode());
        contract.setRemark(remark);
        contract.setEndTimeTs(endTimeTs);
        contract.setVal(val);

        contractService.update(contract);

        return success(contract);
    }

    /**
     * 用户打款补充交易hash
     */
    @PostMapping("detailTx")
    public Object detailTx(@RequestBody Map map) {
        Long id = getLong(map, "id");
        Contract contract = contractService.findById(id);
        if (contract == null || !contract.getUserId().equals(getUser().getId())) {
            return fail("非法权限!");
        }

        String tx = getString(map, "tx");
        if (!noEmptyString(tx)) {
            return fail("信息输入不完整!");
        }

        if (ContractStatusEnum.valueOf(contract.getState()) != ContractStatusEnum.OFFICE_CONFRIMED) {
            return fail("信贷机构尚未确认合约有效");
        }

        contract.setTx(tx);
        contract.setState(ContractStatusEnum.USER_CONFIRMED.getCode());
        contractService.update(contract);
        return success();
    }


}
