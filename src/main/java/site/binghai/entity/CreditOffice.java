package site.binghai.entity;

import lombok.Data;

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
public class CreditOffice extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String nameHash; // 信贷机构名字hash
    private String uuid; // 信贷机构唯一识别号
    private String account;
    private String pass;
    private String smartContractAddrsss; // 只能合约地址

    public CreditOffice() {
        uuid = UUID.randomUUID().toString();
    }
}
