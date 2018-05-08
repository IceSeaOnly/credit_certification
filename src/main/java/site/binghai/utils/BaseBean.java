package site.binghai.utils;

/**
 * Created by IceSea on 2018/5/7.
 * GitHub: https://github.com/IceSeaOnly
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by IceSea on 2018/5/5.
 * GitHub: https://github.com/IceSeaOnly
 */
public abstract class BaseBean extends MapUtils {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected JSONObject newJSONObject() {
        return new JSONObject();
    }

    protected JSONArray newJSONArray() {
        return new JSONArray();
    }

    protected List emptyList() {
        return new ArrayList();
    }

    protected JSONObject toJsonObject(Object obj) {
        return JSONObject.parseObject(JSONObject.toJSONString(obj));
    }

    protected boolean noEmptyString(Collection<String> strs){
        for (String str : strs) {
            if(StringUtils.isEmpty(str))
                return false;
        }
        return true;
    }

    protected boolean noEmptyString(String... strs){
        for (String str : strs) {
            if(StringUtils.isEmpty(str))
                return false;
        }
        return true;
    }
}

