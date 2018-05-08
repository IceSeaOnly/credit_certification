package site.binghai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.binghai.entity.CreditOffice;
import site.binghai.entity.User;
import site.binghai.service.CreditOfficeService;
import site.binghai.service.UserService;
import site.binghai.utils.MD5;

import java.util.Map;

/**
 * Created by IceSea on 2018/5/7.
 * GitHub: https://github.com/IceSeaOnly
 */
@RestController
@RequestMapping("/login/")
public class LoginController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private CreditOfficeService creditOfficeService;

    @GetMapping("isLogin")
    public Object isLogin() {
        if (getCreditOfficer() == null && getUser() == null) {
            return fail("NOT LOGIN");
        }
        return getCreditOfficer() == null ? success(getUser(), "0") : success(getCreditOfficer(), "1");
    }

    @GetMapping("logout")
    public Object logout() {
        getSession().invalidate();
        return success();
    }

    @PostMapping("userLogin")
    public Object userLogin(@RequestBody Map map) {
        User user = userService.newInstance(map);
        if (noEmptyString(user.getPass(), user.getPhone())) {
            user = userService.login(user);
            if (user == null) {
                return fail("手机号/密码不正确-1");
            }
            getSession().setAttribute("user", user);
            return success();
        }
        return fail("手机号/密码不正确-2");
    }

    @PostMapping("officeLogin")
    public Object officeLogin(@RequestBody Map map) {
        CreditOffice office = creditOfficeService.newInstance(map);
        if (noEmptyString(office.getPass(), office.getAccount())) {
            office = creditOfficeService.login(office.getAccount(), office.getPass());
            if (office == null) {
                return fail("账号/密码不正确-1");
            }
            getSession().setAttribute("office", office);
            return success();
        }
        return fail("账号/密码不正确-2");
    }

    @PostMapping("userReg")
    public Object userReg(@RequestBody Map map) {
        map.remove("uuid");

        User user = userService.newInstance(map);
        if (!noEmptyString(user.getPhone(), user.getPass(), user.getName())) {
            return fail("输入不完整!");
        }

        if (!userService.uniqueUser(user.getPhone())) {
            return fail("手机号重复无法注册!");
        }

        userService.save(user);
        return success();
    }

    @PostMapping("officeReg")
    public Object officeReg(@RequestBody Map map) {
        map.remove("uuid");

        CreditOffice office = creditOfficeService.newInstance(map);

        if (!noEmptyString(office.getAccount(), office.getPass(), office.getName())) {
            return fail("输入不完整!");
        }

        office.setNameHash(MD5.encryption(office.getName()));

        if (!creditOfficeService.unique(office)) {
            return fail("信息重复无法注册!");
        }

        creditOfficeService.save(office);
        return success();
    }
}
