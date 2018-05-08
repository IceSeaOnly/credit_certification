package site.binghai.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import site.binghai.Enums.ContractStatusEnum;
import site.binghai.entity.Contract;

import java.util.List;

/**
 * Created by IceSea on 2018/5/7.
 * GitHub: https://github.com/IceSeaOnly
 */
@Service
public class ContractService extends BaseService<Contract> {
    public List<Contract> findByUserId(Long userId) {
        Contract contract = new Contract();
        contract.setUuid(null);
        contract.setSysRemark(null);
        contract.setUserId(userId);
        return query(contract);
    }

    public JSONObject toJSONObject(Contract contract) {
        JSONObject obj = toJsonObject(contract);
            obj.put("states", ContractStatusEnum.valueOf(contract.getState()).getName());
        return obj;
    }

    public List<Contract> findByOfficeId(Long id) {
        Contract contract = new Contract();
        contract.setUuid(null);
        contract.setOfficeId(id);
        contract.setSysRemark(null);
        return query(contract);
    }

    public List<Contract> findByState(ContractStatusEnum statusEnum) {
        Contract contract = new Contract();
        contract.setUuid(null);
        contract.setState(statusEnum.getCode());
        return query(contract);
    }
}
