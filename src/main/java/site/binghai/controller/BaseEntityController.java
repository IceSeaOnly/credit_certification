package site.binghai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import site.binghai.entity.BaseEntity;
import site.binghai.service.BaseService;

import java.util.Map;

/**
 * Created by IceSea on 2018/5/7.
 * GitHub: https://github.com/IceSeaOnly
 */
public abstract class BaseEntityController<T extends BaseEntity> extends BaseController {
    protected abstract BaseService<T> getService();

    protected void beforList() throws Exception {
    }

    protected void beforeAdd(Map map) throws Exception {
    }

    protected void afterAdd(T t) throws Exception {
    }

    protected void beforeUpdate(Map map) throws Exception {
    }

    protected void afterUpdate(T t) throws Exception {
    }

    protected void beforeDelete(Long id) throws Exception {
    }

    protected void afterDelete(Long id) throws Exception {
    }


    @GetMapping("list")
    public Object list(Integer page, Integer pageSize) {
        try {
            beforList();
            return getService().findAll(page == null ? 0 : page, pageSize == null ? 10 : pageSize);
        } catch (Exception e) {
            logger.error("list failed!", e);
            return fail(e.getMessage());
        }
    }

    @PostMapping("update")
    public Object update(@RequestBody Map map) {
        try {
            beforeUpdate(map);
            T t = getService().updateAndSave(map);
            afterUpdate(t);
            return success(t, "SUCCESS");
        } catch (Exception e) {
            logger.error("update failed!", e);
            return fail(e.getMessage());
        }
    }

    @GetMapping("delete")
    public Object delete(@RequestParam Long id) {
        try {
            beforeDelete(id);
            getService().delete(id);
            afterDelete(id);
            return success();
        } catch (Exception e) {
            logger.error("delete failed!", e);
            return fail(e.getMessage());
        }
    }

    @PostMapping("add")
    public Object add(@RequestBody Map map) {
        try {
            beforeAdd(map);
            T t = getService().newInstance(map);
            t = getService().save(t);
            afterAdd(t);
            return success(t, "SUCCESS");
        } catch (Exception e) {
            logger.error("add failed!", e);
            return fail(e.getMessage());
        }
    }
}
