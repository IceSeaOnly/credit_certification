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
public class User extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String phone;
    private String pass;
    private String uuid; // 普通用户唯一识别号

    public User() {
        uuid = UUID.randomUUID().toString();
    }
}
