package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.constant.ParameterValues;
import com.nbicc.cu.carsunion.dao.AdminDao;
import com.nbicc.cu.carsunion.dao.MerchantDao;
import com.nbicc.cu.carsunion.dao.UserDao;
import com.nbicc.cu.carsunion.dao.UserQueryProductDao;
import com.nbicc.cu.carsunion.model.Admin;
import com.nbicc.cu.carsunion.model.Merchant;
import com.nbicc.cu.carsunion.model.User;
import com.nbicc.cu.carsunion.model.UserQueryProduct;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.MessageDigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

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

    @Autowired
    private UserQueryProductDao userQueryProductDao;

    public Admin getAdminByUserNameAndAuthority(String userName, int authority) {
        return adminDao.findByUserNameAndAuthority(userName, authority);
    }

    public Admin validatePassword(Admin admin, String password) {
        if (admin == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder(admin.getUserName());
        sb.append(admin.getUserPasswd().toLowerCase());
        String md5 = MessageDigestUtil.MD5Encode(sb.toString(), null);
        if (md5 == null || !md5.equals(password)) {
            return null;
        } else {
            return admin;
        }
    }

    public String getToken(RedisTemplate redisTemplate, String id){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String token = CommonUtil.generateUUID32();
        valueOperations.set("token"+token, id);
        redisTemplate.expire("token"+token, ParameterValues.TOKEN_EXPIRE_VALUE, TimeUnit.HOURS);
        return token;
    }

    public String getUpdateToken(RedisTemplate redisTemplate, String id){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String token = CommonUtil.generateUUID32();
        valueOperations.set("updateToken"+token, id);
        redisTemplate.expire("updateToken"+token, ParameterValues.UPDATETOKEN_EXPIRE_VALUE, TimeUnit.DAYS);
        return token;
    }

    public String updateToken(RedisTemplate redisTemplate, String userId, String updateToken){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String tokenValue = (String) valueOperations.get("updateToken" + updateToken);
        if(!CommonUtil.isNullOrEmpty(tokenValue) && tokenValue.equals(userId)){
            return getToken(redisTemplate,userId);
        }else{
            return null;
        }
    }

    public Merchant getMerchantById(String id) {
        return merchantDao.findById(id);
    }

    public User getUserById(String id) {
        return userDao.findById(id);
    }

    public Admin getById(String userId) {
        return adminDao.findById(userId);
    }

    public Page<UserQueryProduct> getAllQueriedProducts(int pageNum,int pageSize){
        Sort sort = new Sort(Sort.Direction.DESC, "queryTime");
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);
        return userQueryProductDao.findAll(pageable);
    }
}
