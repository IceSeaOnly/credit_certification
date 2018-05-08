package site.binghai.entity;

import lombok.Data;
import site.binghai.Enums.ContractStatusEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

/**
 * Created by IceSea on 2018/5/7.
 * GitHub: https://github.com/IceSeaOnly
 */
@Data
@Entity
public class Contract extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String uuid; // 合约参考码
    private String smartContractAddrsss; // 该合约所在地址
    private String tx; // 交易hash
    @Column(name = "_from")
    private String from; // 付款方
    private Long val; // 交易额,大整数
    private Long endTimeTs; // 合约到期时间戳

    private Long userId; // 用户id
    private Long officeId; // 机构id
    private String userName;
    private String officeName;

    @Column(columnDefinition = "TEXT")
    private String remark;
    private String sysRemark;

    /**
     * {@link ContractStatusEnum}
     **/
    private Integer state;  // 合约状态


    public Contract() {
        uuid = UUID.randomUUID().toString();
    }
}
