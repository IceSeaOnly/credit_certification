package site.binghai.controller;

import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.binghai.Enums.ContractStatusEnum;
import site.binghai.entity.Contract;
import site.binghai.service.ContractService;
import site.binghai.service.CreditOfficeService;

import java.util.List;
import java.util.Map;

/**
 * Created by IceSea on 2018/5/7.
 * GitHub: https://github.com/IceSeaOnly
 */
@RestController
@RequestMapping("/office/")
public class OfficeController extends BaseController {
    @Autowired
    private CreditOfficeService officeService;
    @Autowired
    private ContractService contractService;

    @GetMapping("list")
    public Object list(Long id) {
        if (id != null) {
            Contract contract = contractService.findById(id);
            if (!contract.getOfficeId().equals(getCreditOfficer().getId())) {
                return fail("无权访问!");
            }
            return success(contractService.toJSONObject(contract));
        }
        List<Contract> contracts = contractService.findByOfficeId(getCreditOfficer().getId());
        JSONArray array = newJSONArray();
        contracts.forEach(v -> array.add(contractService.toJSONObject(v)));
        return success(array);
    }

    /**
     * 信贷机构确认合约有效
     */
    @GetMapping("confirmContract")
    public Object confirmContract(@RequestParam Long id) {
        Contract contract = contractService.findById(id);
        if (!contract.getOfficeId().equals(getCreditOfficer().getId())) {
            return fail("无权访问!");
        }

        logger.warn("{} confirmed the contract:{}", getCreditOfficer(), contract);
        contract.setState(ContractStatusEnum.OFFICE_CONFRIMED.getCode());

        contractService.update(contract);
        return success();
    }

    /**
     * 拒绝并删除合约
     * */
    @GetMapping("delContract")
    public Object delContract(@RequestParam Long id){
        Contract contract = contractService.findById(id);
        if (!contract.getOfficeId().equals(getCreditOfficer().getId())) {
            return fail("无权访问!");
        }

        logger.warn("{} deleted the contract:{}", getCreditOfficer(), contract);

        contractService.delete(id);
        return success();

    }
}
