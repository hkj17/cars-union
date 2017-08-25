package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.dao.AdminDao;
import com.nbicc.cu.carsunion.dao.MerchantDao;
import com.nbicc.cu.carsunion.dao.UserDao;
import com.nbicc.cu.carsunion.model.Admin;
import com.nbicc.cu.carsunion.model.Merchant;
import com.nbicc.cu.carsunion.model.User;
import com.nbicc.cu.carsunion.util.MessageDigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by bigmao on 2017/8/18.
 */
@Service
public class AdminService {

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private MerchantDao merchantDao;

    @Autowired
    private UserDao userDao;

    public Admin getAdminByUserNameAndAuthority(String userName, int authority) {
        return adminDao.findByUserNameAndAuthority(userName, authority);
    }

    public Admin validatePassword(Admin admin, String password) {
        if (admin == null) {
            return null;
        }
        String md5 = MessageDigestUtil.MD5Encode(password, null);
        if (md5 == null || !md5.equals(admin.getUserPasswd())) {
            return null;
        }else{
            return admin;
        }
    }

    public Merchant getMerchantById(String id) {
        return merchantDao.findById(id);
    }

    public User getUserById(String id){
        return userDao.findById(id);
    }
}
