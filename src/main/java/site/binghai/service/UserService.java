package site.binghai.service;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import site.binghai.entity.User;

/**
 * Created by IceSea on 2018/5/7.
 * GitHub: https://github.com/IceSeaOnly
 */
@Service
public class UserService extends BaseService<User> {

    public User login(User user) {
        user.setId(null);
        user.setUuid(null);
        return queryOne(user);
    }

    public boolean uniqueUser(String phone) {
        User user = new User();
        user.setUuid(null);
        user.setPhone(phone);

        return CollectionUtils.isEmpty(query(user));
    }
}
