package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.dao.AdminDao;
import com.nbicc.cu.carsunion.dao.MerchantDao;
import com.nbicc.cu.carsunion.http.HttpRequest;
import com.nbicc.cu.carsunion.http.RegionalInfoHttpRequest;
import com.nbicc.cu.carsunion.http.data.RegionalInfo;
import com.nbicc.cu.carsunion.model.Merchant;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.plaf.synth.Region;
import java.util.List;

@Service
public class MerchantService {

    private RegionalInfoHttpRequest httpRequest = new RegionalInfoHttpRequest();

    @Autowired
    AdminDao adminDao;

    @Autowired
    MerchantDao merchantDao;

    public List<RegionalInfo> getRegionalInfoList(String province, String city, String district){
        return httpRequest.getDistricts(province,city,district);
    }

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
        m.setIdcardFront(idcardFront);
        m.setIdcardBack(idcardBack);
        m.setLicensePath(license);
        merchantDao.save(m);
        return true;
    }
}
