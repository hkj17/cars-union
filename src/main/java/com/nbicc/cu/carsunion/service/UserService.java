package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.dao.*;
import com.nbicc.cu.carsunion.model.*;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.MessageDigestUtil;
import com.nbicc.cu.carsunion.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    AdminDao adminDao;

    @Autowired
    UserDao userDao;

    @Autowired
    UserVehicleRelationshipDao userVehicleRelationshipDao;

    @Autowired
    VehicleDao vehicleDao;

    @Autowired
    AddressDao addressDao;

    @Autowired
    VipLevelDao vipLevelDao;

    @Autowired
    CreditHistoryDao creditHistoryDao;

    public String validateToken(RedisTemplate redisTemplate, String token) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        return (String) valueOperations.get("token" + token);
    }

    public int updatePassword(RedisTemplate redisTemplate, String id, String oldPassword, String newPassword, String smsCode) {
        Admin admin = adminDao.findById(id);
        if (admin == null) {
            return ParameterKeys.REQUEST_FAIL;
        }
        if (!SmsUtil.verifySmsCode(redisTemplate, admin.getUserName(), smsCode)) {
            return ParameterKeys.FAIL_SMS_VERIFICATION;
        }
        if (admin.getUserPasswd() == null || !admin.getUserPasswd().equals(MessageDigestUtil.MD5Encode(oldPassword, null))) {
            return ParameterKeys.REQUEST_FAIL;
        }
        admin.setUserPasswd(MessageDigestUtil.MD5Encode(newPassword, null));
        adminDao.save(admin);
        return ParameterKeys.REQUEST_SUCCESS;
    }

    @Transactional
    public int userRegister(RedisTemplate redisTemplate, String name, String nickName, String contact, String portrait, String recommend, String password, String smsCode) {
        if (!SmsUtil.verifySmsCode(redisTemplate, contact, smsCode)) {
            return ParameterKeys.FAIL_SMS_VERIFICATION;
        }
        User user = userDao.findByContact(contact);
        if (!CommonUtil.isNullOrEmpty(user)) {
            return ParameterKeys.PHONE_ALREADY_REGISTER;
        }

        user = new User();
        String id = CommonUtil.generateUUID32();
        user.setId(id);
        user.setContact(contact);
        String defaultName = "用户" + contact.substring(7);
        if (!CommonUtil.isNullOrEmpty(name)) {
            user.setName(name);
        } else {
            user.setName(defaultName);
        }
        if (!CommonUtil.isNullOrEmpty(nickName)) {
            user.setNickname(nickName);
        } else {
            user.setNickname(defaultName);
        }
        if (!CommonUtil.isNullOrEmpty(portrait)) {
            user.setPortraitPath(portrait);
        }
        if (!CommonUtil.isNullOrEmpty(recommend)) {
            user.setRecommend(recommend);
        }
        user.setCredit(0);
        userDao.save(user);
        Admin admin = new Admin();
        admin.setUserName(contact);
        admin.setUserPasswd(MessageDigestUtil.MD5Encode(password, null));
        admin.setId(id);
        admin.setAuthority(2);
        adminDao.save(admin);
        return ParameterKeys.REQUEST_SUCCESS;
    }

    @Transactional
    public int modifyUserInfo(RedisTemplate redisTemplate, String id, String name, String nickName, String contact, String portrait, String smsCode) {
        User user = userDao.findById(id);
        if (CommonUtil.isNullOrEmpty(user)) {
            return ParameterKeys.REQUEST_FAIL;
        }
        if (!CommonUtil.isNullOrEmpty(name)) {
            user.setName(name);
        }
        if (!CommonUtil.isNullOrEmpty(nickName)) {
            user.setNickname(nickName);
        }
        if (!CommonUtil.isNullOrEmpty(portrait)) {
            user.setPortraitPath(portrait);
        }
        if (!CommonUtil.isNullOrEmpty(contact)) {
            boolean passSmsVerification = SmsUtil.verifySmsCode(redisTemplate, contact, smsCode);
            if (!passSmsVerification) {
                return ParameterKeys.FAIL_SMS_VERIFICATION;
            } else {
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
    public boolean addAddress(String userId, String addr, Boolean isDefault) {
        User user = userDao.findById(userId);
        if (CommonUtil.isNullOrEmpty(user)) {
            return false;
        }
        Address address = new Address();
        address.setUser(user);
        address.setAddress(addr);
        address.setId(CommonUtil.generateUUID32());
        if (isDefault) {
            Address defaultAddr = addressDao.findByUserAndIsDefault(user, true);
            if (!CommonUtil.isNullOrEmpty(defaultAddr)) {
                defaultAddr.setIsDefault(false);
                addressDao.save(defaultAddr);
            }
            address.setIsDefault(true);
        } else {
            address.setIsDefault(false);
        }
        addressDao.save(address);
        return true;
    }

    @Transactional
    public boolean deleteAddress(String userId, String addressId) {
        Address address = addressDao.findById(addressId);
        if (address == null || !userId.equals(address.getUser().getId())) {
            return false;
        } else {
            addressDao.delete(address);
            return true;
        }
    }

    @Transactional
    public boolean modifyAddress(String userId, String addressId, String name){
        Address address = addressDao.findById(addressId);
        if(address == null || !userId.equals(address.getUser().getId())){
            return false;
        }else{
            address.setAddress(name);
            addressDao.save(address);
            return true;
        }
    }

    @Transactional
    public boolean setDefaultAddress(String userId, String addressId) {
        User user = userDao.findById(userId);
        if (CommonUtil.isNullOrEmpty(user)) {
            return false;
        }
        Address address = addressDao.findById(addressId);
        if (CommonUtil.isNullOrEmpty(address)) {
            return false;
        }
        Address defaultAddr = addressDao.findByUserAndIsDefault(user, true);
        if (!CommonUtil.isNullOrEmpty(defaultAddr)) {
            defaultAddr.setIsDefault(false);
            addressDao.save(defaultAddr);
        }
        address.setIsDefault(true);
        addressDao.save(address);
        return true;
    }

    public List<Address> getAddressList(String userId) {
        User user = userDao.findById(userId);
        if (CommonUtil.isNullOrEmpty(user)) {
            return new ArrayList<Address>();
        } else {
            return user.getAddressList();
        }
    }

    public Set<Vehicle> getVehicleList(String userId) {
        User user = userDao.findById(userId);
        if (CommonUtil.isNullOrEmpty(user)) {
            return new HashSet<Vehicle>();
        } else {
            return user.getVehicles();
        }
    }

    @Transactional
    public boolean addVehicle(String userId, String vehicleId, Boolean isDefault) {
        User user = userDao.findById(userId);
        Vehicle vehicle = vehicleDao.findById(vehicleId);
        if (user == null || vehicle == null) {
            return false;
        }

        UserVehicleRelationship userVehicleRelationship = userVehicleRelationshipDao.findByUserAndVehicle(user, vehicle);
        if (userVehicleRelationship != null) {
            return false;
        }

        userVehicleRelationship = new UserVehicleRelationship();
        userVehicleRelationship.setUser(user);
        userVehicleRelationship.setVehicle(vehicle);
        if (isDefault) {
            UserVehicleRelationship uvr = userVehicleRelationshipDao.findByUserAndIsDefault(user, true);
            if (uvr != null) {
                uvr.setIsDefault(false);
                userVehicleRelationshipDao.save(uvr);
            }
            userVehicleRelationship.setIsDefault(true);
        } else {
            userVehicleRelationship.setIsDefault(false);
        }
        userVehicleRelationshipDao.save(userVehicleRelationship);
        return true;
    }

    @Transactional
    public boolean deleteVehicle(String userId, String vehicleId) {
        User user = userDao.findById(userId);
        Vehicle vehicle = vehicleDao.findById(vehicleId);
        if (user == null || vehicle == null) {
            return false;
        }

        UserVehicleRelationship uvr = userVehicleRelationshipDao.findByUserAndVehicle(user, vehicle);
        if (uvr == null) {
            return false;
        } else {
            userVehicleRelationshipDao.delete(uvr);
            return true;
        }
    }

    @Transactional
    public boolean setDefaultVehicle(String userId, String vehicleId) {
        User user = userDao.findById(userId);
        if (CommonUtil.isNullOrEmpty(user)) {
            return false;
        }
        Vehicle vehicle = vehicleDao.findById(vehicleId);
        if (CommonUtil.isNullOrEmpty(vehicle)) {
            return false;
        }

        UserVehicleRelationship uvr_old = userVehicleRelationshipDao.findByUserAndIsDefault(user, true);
        UserVehicleRelationship uvr = userVehicleRelationshipDao.findByUserAndVehicle(user, vehicle);
        if (uvr == null) {
            return false;
        }
        uvr.setIsDefault(true);
        uvr_old.setIsDefault(false);
        userVehicleRelationshipDao.save(uvr);
        userVehicleRelationshipDao.save(uvr_old);
        return true;
    }

    public Vehicle getDefaultVehicle(String userId) {
        User user = userDao.findById(userId);
        if (CommonUtil.isNullOrEmpty(user)) {
            return null;
        }
        UserVehicleRelationship uvr = userVehicleRelationshipDao.findByUserAndIsDefault(user, true);
        if (CommonUtil.isNullOrEmpty(uvr)) {
            return null;
        }
        return uvr.getVehicle();
    }

    public VipLevel getVipLevelByUser(String userId) {
        User user = userDao.findById(userId);
        if (CommonUtil.isNullOrEmpty(user)) {
            return null;
        } else {
            return vipLevelDao.findVipLevelByRange(user.getCredit());
        }
    }

    public List<CreditHistory> getUserCreditHistory(String userId, int source){
        return creditHistoryDao.findByUserIdAndAndSource(userId,source);
    }
}
