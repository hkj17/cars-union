package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.constant.ParameterValues;
import com.nbicc.cu.carsunion.dao.AdminDao;
import com.nbicc.cu.carsunion.dao.MerchantDao;
import com.nbicc.cu.carsunion.model.Admin;
import com.nbicc.cu.carsunion.model.Merchant;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.MessageDigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class MerchantService {

    @Autowired
    AdminDao adminDao;

    @Autowired
    MerchantDao merchantDao;

    public boolean merchantRegister(String name, String address, String region, String contact,
                                    String longitude, String latitude, String idcardFront, String idcardBack,
                                    String license){
        Merchant m = merchantDao.findByContact(contact);
        if(CommonUtil.isNullOrEmpty(m)){
            m = new Merchant();
            m.setId(CommonUtil.generateUUID32());
            m.setContact(contact);
        }
        m.setName(name);
        m.setAddress(address);
        m.setRegion(region);
        m.setLongitude(longitude);
        m.setLatitude(latitude);
        m.setRegStatus(0);
        m.setIdcardFront(idcardFront);
        m.setIdcardBack(idcardBack);
        m.setLicensePath(license);
        merchantDao.save(m);
        return true;
    }

    public boolean passRegistration(String contact){
        Merchant m = merchantDao.findByContact(contact);
        if(CommonUtil.isNullOrEmpty(m)){
            return false;
        }
        m.setRegStatus(1);
        Timestamp regTime = new Timestamp(System.currentTimeMillis());
        m.setRegTime(regTime);
        merchantDao.save(m);

        Admin a = new Admin();
        a.setId(m.getId());
        a.setUserName(contact);
        a.setUserPasswd(MessageDigestUtil.MD5Encode(ParameterValues.DEFAULT_PASSWD, null));
        a.setAuthority(1);
        adminDao.save(a);
        return true;
    }

    public boolean failRegistration(String contact){
        Merchant m = merchantDao.findByContact(contact);
        if(CommonUtil.isNullOrEmpty(m)){
            return false;
        }
        m.setRegStatus(2);
        merchantDao.save(m);
        return true;
    }

    public List<Merchant> getRegInProcessList(){
        return merchantDao.findRegInProcessList();
    }
}
