package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.constant.ParameterValues;
import com.nbicc.cu.carsunion.dao.AdminDao;
import com.nbicc.cu.carsunion.dao.MerchantDao;
import com.nbicc.cu.carsunion.enumtype.RegisterStatus;
import com.nbicc.cu.carsunion.model.Admin;
import com.nbicc.cu.carsunion.model.Merchant;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.MessageDigestUtil;
import com.nbicc.cu.carsunion.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MerchantService {

    @Autowired
    AdminDao adminDao;

    @Autowired
    MerchantDao merchantDao;

    @Autowired
    @PersistenceContext
    private EntityManager em;

    public int merchantRegister(RedisTemplate redisTemplate, String name, String address, String region, String contact,
                                String longitude, String latitude, String idcardFront, String idcardBack, String license, String smsCode) {
        Merchant m = merchantDao.findByContact(contact);
        if(!CommonUtil.isNullOrEmpty(m) && m.getRegStatus() == RegisterStatus.PASSED_REVIEW.ordinal()){
            //已通过注册，请登录
            return ParameterKeys.PHONE_ALREADY_REGISTER;
        }

        if (CommonUtil.isNullOrEmpty(m)) {
            m = new Merchant();
            m.setId(CommonUtil.generateUUID32());
            m.setContact(contact);
        }
        if (!SmsUtil.verifySmsCode(redisTemplate, contact, smsCode)) {
            return ParameterKeys.FAIL_SMS_VERIFICATION;
        }
        m.setName(name);
        m.setAddress(address);
        m.setRegion(region);
        m.setLongitude(longitude);
        m.setLatitude(latitude);
        m.setRegStatus(RegisterStatus.UNDER_REVIEW.ordinal());
        m.setIdcardFront(idcardFront);
        m.setIdcardBack(idcardBack);
        m.setLicensePath(license);
        merchantDao.save(m);
        return ParameterKeys.REQUEST_SUCCESS;
    }

    @Transactional
    public boolean passRegistration(String contact) {
        Merchant m = merchantDao.findByContact(contact);
        if (CommonUtil.isNullOrEmpty(m)) {
            return false;
        }
        m.setRegStatus(RegisterStatus.PASSED_REVIEW.ordinal());
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

    public boolean failRegistration(String contact) {
        Merchant m = merchantDao.findByContact(contact);
        if (CommonUtil.isNullOrEmpty(m)) {
            return false;
        }
        m.setRegStatus(RegisterStatus.FAILED_REVIEW.ordinal());
        merchantDao.save(m);
        return true;
    }

    public Page<Merchant> getRegInProcessList(int status, int pageNum, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(pageNum - 1, pageSize, sort);
        return merchantDao.findByRegStatus(status, pageable);
    }

    @Transactional
    public boolean modifyMerchantInfo(String id, String name, String address, String region, String contact, String longitude, String latitude) {
        Merchant m = merchantDao.findById(id);
        if (CommonUtil.isNullOrEmpty(m)) {
            return false;
        }

        if (!CommonUtil.isNullOrEmpty(name)) {
            m.setName(name);
        }
        if (!CommonUtil.isNullOrEmpty(address)) {
            m.setAddress(address);
        }
        if (!CommonUtil.isNullOrEmpty(region)) {
            m.setRegion(region);
        }
        if (!CommonUtil.isNullOrEmpty(contact)) {
            Admin admin = adminDao.findByUserNameAndAuthority(contact, 1);
            if (admin != null) {
                admin.setUserName(contact);
                adminDao.save(admin);
            } else {
                return false;
            }
            m.setContact(contact);

        }
        if (!CommonUtil.isNullOrEmpty(longitude)) {
            m.setLongitude(longitude);
        }
        if (!CommonUtil.isNullOrEmpty(latitude)) {
            m.setLatitude(latitude);
        }
        merchantDao.save(m);
        return true;
    }

    public Merchant getMerchantByContact(String contact) {
        return merchantDao.findByContact(contact);
    }

    public List<Merchant> getMerchantList(String region, String keyword) {
        String sql = "from Merchant m where m.regStatus = 1";
        Map<Integer, Object> paramMap = new HashMap<Integer, Object>();
        int i = 1;
        if (!CommonUtil.isNullOrEmpty(region)) {
            sql += " and m.region like ?" + i;
            paramMap.put(i++, "%" + region + "%");
        }

        if (!CommonUtil.isNullOrEmpty(keyword)) {
            sql += " and m.name like ?" + i;
            paramMap.put(i++, "%" + keyword + "%");
        }

        Query query = em.createQuery(sql);
        for (int p = 1; p <= paramMap.size(); p++) {
            query.setParameter(p, paramMap.get(p));
        }
        return query.getResultList();
    }
}
