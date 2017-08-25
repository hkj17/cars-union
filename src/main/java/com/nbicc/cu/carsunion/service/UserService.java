package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.dao.AdminDao;
import com.nbicc.cu.carsunion.dao.UserDao;
import com.nbicc.cu.carsunion.model.Admin;
import com.nbicc.cu.carsunion.model.User;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {

    @Autowired
    AdminDao adminDao;

    @Autowired
    UserDao userDao;

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
}
