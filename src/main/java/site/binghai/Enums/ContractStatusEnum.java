package site.binghai.Enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IceSea on 2018/5/7.
 * GitHub: https://github.com/IceSeaOnly
 */
public enum ContractStatusEnum {
    CREATED(0,"已创建，待补充详细信息"),
    DETAILED(1,"已补充详细信息"),
    OFFICE_CONFRIMED(2,"信贷机构确认合约有效,待用户确认打款"),
    USER_CONFIRMED(3,"用户确认打款,待系统确认是否到账"),
    SYSTEM_CONFRIMED(4,"系统确认到账,履行合约中"),
    CONTRACT_EXECUTING(5,"履行合约中"),
    COMPLETED(6,"合约已完成,请及时解约提币"),

    ;
    private Integer code;
    private String name;
    private static Map<Integer, Object> maps;

    static {
        maps = new HashMap<>();
        for (ContractStatusEnum c : ContractStatusEnum.values()) {
            maps.put(c.getCode(), c);
        }
    }

    public static ContractStatusEnum valueOf(Integer code) {
        Object obj = maps.get(code);
        return (ContractStatusEnum) obj;
    }

    ContractStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
