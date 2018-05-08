package site.binghai.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by IceSea on 2018/5/8.
 * GitHub: https://github.com/IceSeaOnly
 */
@RestController
@RequestMapping("cache")
public class CacheController extends BaseController {
    @PostMapping("setV")
    public Object setV(@RequestBody Map map) {
        getSession().setAttribute("T_V", map.get("V"));
        return success();
    }

    @GetMapping("getV")
    public Object getV() {
        Object o = getSession().getAttribute("T_V");
        return o == null ? fail("NOT SET!") : success(o);
    }

    @GetMapping("clearV")
    public Object clearV() {
        getSession().removeAttribute("T_V");
        return success();
    }
}
