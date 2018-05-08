package site.binghai.service;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import site.binghai.entity.CreditOffice;

/**
 * Created by IceSea on 2018/5/7.
 * GitHub: https://github.com/IceSeaOnly
 */
@Service
public class CreditOfficeService extends BaseService<CreditOffice> {

    public CreditOffice login(String accout, String pass) {
        CreditOffice office = new CreditOffice();
        office.setUuid(null);
        office.setAccount(accout);
        office.setPass(pass);

        return queryOne(office);
    }

    public boolean unique(CreditOffice thiz) {
        CreditOffice office = new CreditOffice();
        office.setUuid(null);
        office.setNameHash(thiz.getNameHash());
        if (!CollectionUtils.isEmpty(query(office))) return false;

        office.setNameHash(null);
        office.setAccount(thiz.getAccount());

        return CollectionUtils.isEmpty(query(office));
    }
}
