package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.dao.*;
import com.nbicc.cu.carsunion.enumtype.ResponseType;
import com.nbicc.cu.carsunion.model.*;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.MessageDigestUtil;
import com.nbicc.cu.carsunion.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Autowired
    FavoriteDao favoriteDao;

    @Autowired
    ProductDao productDao;

    @Autowired
    private UserCreditDao userCreditDao;

    public String validateToken(RedisTemplate redisTemplate, String token) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        return (String) valueOperations.get("token" + token);
    }

    public ResponseCode updatePassword(RedisTemplate redisTemplate, String id, String oldPassword, String newPassword, String smsCode) {
        Admin admin = adminDao.findById(id);
        if (admin == null) {
            return new ResponseCode(ResponseType.REQUEST_FAIL,"用户不存在");
        }
        if (!SmsUtil.verifySmsCode(redisTemplate, admin.getUserName(), smsCode)) {
            return new ResponseCode(ResponseType.FAIL_SMS_VERIFICATION,"手机验证码错误");
        }
        if (admin.getUserPasswd() == null || !admin.getUserPasswd().equals(MessageDigestUtil.MD5Encode(oldPassword, null))) {
            return new ResponseCode(ResponseType.REQUEST_FAIL,"密码错误或为空");
        }
        admin.setUserPasswd(MessageDigestUtil.MD5Encode(newPassword, null));
        adminDao.save(admin);
        return new ResponseCode(ResponseType.REQUEST_SUCCESS,"更改密码成功");
    }

    @Transactional
    public ResponseCode userRegister(RedisTemplate redisTemplate, String name, String nickName, String contact, String region, String portrait, String recommend, String password, String smsCode) {
        if (!SmsUtil.verifySmsCode(redisTemplate, contact, smsCode)) {
            return new ResponseCode(ResponseType.FAIL_SMS_VERIFICATION,"手机验证码错误");
        }
        User user = userDao.findByContact(contact);
        if (!CommonUtil.isNullOrEmpty(user)) {
            return new ResponseCode(ResponseType.PHONE_ALREADY_REGISTER,"用户手机已注册");
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
        if (!CommonUtil.isNullOrEmpty(region)) {
            user.setRegion(region);
        }
        if (!CommonUtil.isNullOrEmpty(portrait)) {
            user.setPortraitPath(portrait);
        }
        if (!CommonUtil.isNullOrEmpty(recommend)) {
            user.setRecommend(recommend);
        }
        userDao.save(user);
        Admin admin = new Admin();
        admin.setUserName(contact);
        admin.setUserPasswd(MessageDigestUtil.MD5Encode(password, null));
        admin.setId(id);
        admin.setAuthority(2);
        adminDao.save(admin);
        return new ResponseCode(ResponseType.REQUEST_SUCCESS,"注册成功");
    }

    @Transactional
    public ResponseCode modifyUserInfo(RedisTemplate redisTemplate, String id, String name, String nickName, String contact, String region, String portrait, String smsCode) {
        User user = userDao.findById(id);
        if (CommonUtil.isNullOrEmpty(user)) {
            return new ResponseCode(ResponseType.REQUEST_FAIL,"没有该用户");
        }
        if (!CommonUtil.isNullOrEmpty(name)) {
            user.setName(name);
        }
        if (!CommonUtil.isNullOrEmpty(nickName)) {
            user.setNickname(nickName);
        }
        if(!CommonUtil.isNullOrEmpty(region)){
            user.setRegion(region);
        }
        if (!CommonUtil.isNullOrEmpty(portrait)) {
            user.setPortraitPath(portrait);
        }
        if (!CommonUtil.isNullOrEmpty(contact)) {
            boolean passSmsVerification = SmsUtil.verifySmsCode(redisTemplate, contact, smsCode);
            if (!passSmsVerification) {
                return new ResponseCode(ResponseType.FAIL_SMS_VERIFICATION,"手机验证码错误");
            } else {
                Admin admin = adminDao.findById(id);
                admin.setUserName(contact);
                adminDao.save(admin);
                user.setContact(contact);
            }
        }
        userDao.save(user);
        return  new ResponseCode(ResponseType.REQUEST_SUCCESS,"操作成功");
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

    public List<UserVehicleRelationship> getVehicleList(String userId) {
        User user = userDao.findById(userId);
        if (CommonUtil.isNullOrEmpty(user)) {
            return new ArrayList<UserVehicleRelationship>();
        } else {
            return userVehicleRelationshipDao.findByUser(user);
        }
    }

    @Transactional
    public boolean addVehicle(String userId, String vehicleId, String plateNum, Boolean isDefault) {
        User user = userDao.findById(userId);
        Vehicle vehicle = vehicleDao.findById(vehicleId);
        if (user == null || vehicle == null) {
            return false;
        }

        UserVehicleRelationship userVehicleRelationship = userVehicleRelationshipDao.findByUserAndVehicleAndPlateNum(user,vehicle,plateNum);
        if (userVehicleRelationship != null) {
            return false;
        }

        userVehicleRelationship = new UserVehicleRelationship();
        userVehicleRelationship.setUser(user);
        userVehicleRelationship.setVehicle(vehicle);
        userVehicleRelationship.setPlateNum(plateNum);
        userVehicleRelationship.setIsBindMh(false);
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
    public ResponseCode deleteVehicle(String userId, String vehicleId,String plateNum) {
        User user = userDao.findById(userId);
        Vehicle vehicle = vehicleDao.findById(vehicleId);
        if (user == null || vehicle == null) {
            return new ResponseCode(ResponseType.REQUEST_FAIL,"找不到用户或车型");
        }

        UserVehicleRelationship uvr = userVehicleRelationshipDao.findByUserAndVehicleAndPlateNum(user, vehicle,plateNum);
        if (uvr == null) {
            return new ResponseCode(ResponseType.REQUEST_FAIL,"找不到车牌号");
        }else if(uvr.getIsBindMh()){
            return new ResponseCode(ResponseType.BINDED_MH,"已绑定迈鸿设备，请先解绑");
        } else {
            userVehicleRelationshipDao.delete(uvr);
            return new ResponseCode(ResponseType.REQUEST_SUCCESS,"删除成功");
        }
    }

    @Transactional
    public boolean setDefaultVehicle(String userId, String vehicleId,String plateNum) {
        User user = userDao.findById(userId);
        if (CommonUtil.isNullOrEmpty(user)) {
            return false;
        }
        Vehicle vehicle = vehicleDao.findById(vehicleId);
        if (CommonUtil.isNullOrEmpty(vehicle)) {
            return false;
        }

        UserVehicleRelationship uvr_old = userVehicleRelationshipDao.findByUserAndIsDefault(user, true);
        UserVehicleRelationship uvr = userVehicleRelationshipDao.findByUserAndVehicleAndPlateNum(user, vehicle,plateNum);
        if (uvr == null) {
            return false;
        }
        uvr.setIsDefault(true);
        userVehicleRelationshipDao.save(uvr);
        if(!CommonUtil.isNullOrEmpty(uvr_old)) {
            uvr_old.setIsDefault(false);
            userVehicleRelationshipDao.save(uvr_old);
        }
        return true;
    }

    public UserVehicleRelationship getDefaultVehicle(String userId) {
        User user = userDao.findById(userId);
        if (CommonUtil.isNullOrEmpty(user)) {
            return null;
        }
        return userVehicleRelationshipDao.findByUserAndIsDefault(user, true);
    }

    public List<Favorite> getFavoriteList(String userId){
        return favoriteDao.findByUserAndFavorite(userId,true);
    }

    @Transactional
    public boolean addFavorite(String userId, String productId){
        User user = userDao.findById(userId);
        Product product = productDao.findByIdAndDelFlag(productId, 0);
        if (user == null || product == null) {
            return false;
        }
        Favorite favorite = favoriteDao.findByUserAndProduct(user,product);
        if(favorite==null){
            favorite = new Favorite();
            favorite.setId(CommonUtil.generateUUID32());
            favorite.setUser(user);
            favorite.setProduct(product);
        }
        favorite.setCreatedAt(new Date());
        favorite.setFavorite(true);
        favoriteDao.save(favorite);
        return true;
    }

    @Transactional
    public boolean deleteFromFavorite(String userId, List<String> productIdList){
        List<Favorite> favoriteList = favoriteDao.findByUserAndProductIn(userId,productIdList);
        List<Favorite> toRemove = new ArrayList<Favorite>();
        Iterator<Favorite> iter = favoriteList.iterator();
        while(iter.hasNext()){
            Favorite curr = iter.next();
            curr.setFavorite(false);
            if(!curr.isBrowsed()){
                toRemove.add(curr);
                iter.remove();
            }
        }
        favoriteDao.save(favoriteList);
        favoriteDao.deleteInBatch(toRemove);
        return true;
    }

    public List<Favorite> getBrowseHistory(String userId){
        return favoriteDao.findByUserAndBrowsed(userId,true);
    }

    public boolean addToBrowseHistory(String userId, String productId){
        User user = userDao.findById(userId);
        Product product = productDao.findByIdAndDelFlag(productId, 0);
        if (user == null || product == null) {
            return false;
        }
        Favorite favorite = favoriteDao.findByUserAndProduct(user,product);
        if(favorite==null){
            favorite = new Favorite();
            favorite.setId(CommonUtil.generateUUID32());
            favorite.setUser(user);
            favorite.setProduct(product);
        }
        favorite.setLastVisited(new Date());
        favorite.setBrowsed(true);
        favoriteDao.save(favorite);
        return true;
    }

    public boolean deleteFromBrowseHistory(String userId, List<String> productIdList){
        List<Favorite> favoriteList = favoriteDao.findByUserAndProductIn(userId,productIdList);
        List<Favorite> toRemove = new ArrayList<Favorite>();
        Iterator<Favorite> iter = favoriteList.iterator();
        while(iter.hasNext()){
            Favorite curr = iter.next();
            curr.setBrowsed(false);
            if(!curr.isFavorite()){
                toRemove.add(curr);
                iter.remove();
            }
        }
        favoriteDao.save(favoriteList);
        favoriteDao.deleteInBatch(toRemove);
        return true;
    }

    public VipLevel getVipLevelByUser(String userId) {
        User user = userDao.findById(userId);
        if (CommonUtil.isNullOrEmpty(user)) {
            return null;
        } else {
            return vipLevelDao.findVipLevelByRange(userCreditDao.findByUserId(userId).getTotalCredit());
        }
    }



    public String generateInviteCode(String userId, String shareCode) {
        User user = userDao.findById(userId);
        if(!CommonUtil.isNullOrEmpty(user.getShareCode())){
            return "已经存在分享码";
        }
        List<String> shareCodes = userDao.findAllShareCode();
        if(CommonUtil.isNullOrEmpty(shareCode)){
            //生成8位邀请码
            shareCode = UUID.randomUUID().toString().replace("-","").substring(0,8);
            while (shareCodes.contains(shareCode)){
                shareCode = UUID.randomUUID().toString().replace("-","").substring(0,8);
            }
        }else{
            if(shareCodes.contains(shareCode)){
                return "分享码重复，请更换";
            }
        }
        user.setShareCode(shareCode);
        userDao.save(user);
        return shareCode;
    }
}
