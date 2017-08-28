package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.dao.AddressDao;
import com.nbicc.cu.carsunion.dao.AdminDao;
import com.nbicc.cu.carsunion.dao.TokenDao;
import com.nbicc.cu.carsunion.dao.UserDao;
import com.nbicc.cu.carsunion.model.Address;
import com.nbicc.cu.carsunion.model.Admin;
import com.nbicc.cu.carsunion.model.Token;
import com.nbicc.cu.carsunion.model.User;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    AdminDao adminDao;

    @Autowired
    UserDao userDao;

    @Autowired
    AddressDao addressDao;

    @Autowired
    TokenDao tokenDao;

    public String validateToken(String tokenString){
        Token token = tokenDao.findByToken(tokenString);
        if(CommonUtil.isNullOrEmpty(token)){
            return null;
        }
        Timestamp expiresAt = token.getExpiresAt();
        return new Timestamp(System.currentTimeMillis()).before(expiresAt) ? token.getId():null;
    }

    @Transactional
    public int userRegister(HttpServletRequest request, String name, String nickName, String contact, String portrait, String recommend, String password, String smsCode){
        if(!SmsUtil.verifySmsCode(request,contact,smsCode)){
            return ParameterKeys.FAIL_SMS_VERIFICATION;
        }
        User user = userDao.findByContact(contact);
        if(!CommonUtil.isNullOrEmpty(user)){
            return ParameterKeys.REQUEST_FAIL;
        }

        user = new User();
        String id = CommonUtil.generateUUID32();
        user.setId(id);
        user.setContact(contact);
        String defaultName = "用户" + contact.substring(7);
        if(!CommonUtil.isNullOrEmpty(name)){
            user.setName(name);
        }else{
            user.setName(defaultName);
        }
        if(!CommonUtil.isNullOrEmpty(nickName)){
            user.setNickname(nickName);
        }else{
            user.setNickname(defaultName);
        }
        if(!CommonUtil.isNullOrEmpty(portrait)){
            user.setPortraitPath(portrait);
        }
        if(!CommonUtil.isNullOrEmpty(recommend)){
            user.setRecommend(recommend);
        }
        user.setCredit(0);
        userDao.save(user);
        Admin admin = new Admin();
        admin.setUserName(contact);
        admin.setUserPasswd(password);
        admin.setId(id);
        admin.setAuthority(2);
        adminDao.save(admin);
        return ParameterKeys.REQUEST_SUCCESS;
    }

    @Transactional
    public int modifyUserInfo(HttpServletRequest request, String id, String name, String nickName, String contact, String portrait, String smsCode){
        User user = userDao.findById(id);
        if(CommonUtil.isNullOrEmpty(user)){
            return ParameterKeys.REQUEST_FAIL;
        }
        if(!CommonUtil.isNullOrEmpty(name)){
            user.setName(name);
        }
        if(!CommonUtil.isNullOrEmpty(nickName)){
            user.setNickname(nickName);
        }
        if(!CommonUtil.isNullOrEmpty(portrait)){
            user.setPortraitPath(portrait);
        }
        if(!CommonUtil.isNullOrEmpty(contact)){
            boolean passSmsVerification = SmsUtil.verifySmsCode(request,contact,smsCode);
            if(!passSmsVerification){
                return ParameterKeys.FAIL_SMS_VERIFICATION;
            }else{
                Admin admin = adminDao.findById(id);
                admin.setUserName(contact);
                adminDao.save(admin);
                user.setContact(contact);
            }
        }
        userDao.save(user);
        return ParameterKeys.REQUEST_SUCCESS;
    }

    @Transactional
    public boolean addAddress(String userId, String addr, Boolean isDefault){
        User user = userDao.findById(userId);
        if(CommonUtil.isNullOrEmpty(user)){
            return false;
        }
        Address address =  new Address();
        address.setUser(user);
        address.setAddress(addr);
        address.setId(CommonUtil.generateUUID32());
        if(isDefault){
            Address defaultAddr = addressDao.findByUserAndIsDefault(user,true);
            if(!CommonUtil.isNullOrEmpty(defaultAddr)){
                defaultAddr.setIsDefault(false);
                addressDao.save(defaultAddr);
            }
            address.setIsDefault(true);
        }else{
            address.setIsDefault(false);
        }
        addressDao.save(address);
        return true;
    }

    @Transactional
    public boolean setDefaultAddress(String userId, String addressId){
        User user = userDao.findById(userId);
        if(CommonUtil.isNullOrEmpty(user)){
            return false;
        }
        Address address = addressDao.findById(addressId);
        if(CommonUtil.isNullOrEmpty(address)){
            return false;
        }
        Address defaultAddr = addressDao.findByUserAndIsDefault(user,true);
        if(!CommonUtil.isNullOrEmpty(defaultAddr)){
            defaultAddr.setIsDefault(false);
            addressDao.save(defaultAddr);
        }
        address.setIsDefault(true);
        addressDao.save(address);
        return true;
    }

    public List<Address> getAddressList(String userId){
        User user = userDao.findById(userId);
        if(CommonUtil.isNullOrEmpty(user)){
            return new ArrayList<Address>();
        }else{
            return user.getAddressList();
        }
    }
}
