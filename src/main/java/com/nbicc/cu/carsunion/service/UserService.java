package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.dao.*;
import com.nbicc.cu.carsunion.enumtype.ResponseType;
import com.nbicc.cu.carsunion.model.*;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.MessageDigestUtil;
import com.nbicc.cu.carsunion.util.SmsUtil;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.nbicc.cu.carsunion.constant.ParameterValues.ALI_DAYU_APPKEY;
import static com.nbicc.cu.carsunion.constant.ParameterValues.ALI_DAYU_SECRET;
import static com.nbicc.cu.carsunion.constant.ParameterValues.ALI_DAYU_URL;

@Service
public class UserService {

    private static Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserVehicleRelationshipDao userVehicleRelationshipDao;

    @Autowired
    private VehicleDao vehicleDao;

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private VipLevelDao vipLevelDao;

    @Autowired
    private CreditHistoryDao creditHistoryDao;

    @Autowired
    private FavoriteDao favoriteDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserCreditDao userCreditDao;

    @Autowired
    private UserQueryProductDao userQueryProductDao;

    @Autowired
    private UserFeedbackDao userFeedbackDao;

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
    public ResponseCode userRegister(RedisTemplate redisTemplate, String name, String nickName, String contact, String region, String portrait, String shareCode, String password, String smsCode) {
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
        if (!CommonUtil.isNullOrEmpty(shareCode)) {
            User recommendor = userDao.findByShareCode(shareCode);
            if(!CommonUtil.isNullOrEmpty(recommendor)) {
                user.setRecommend(recommendor.getId());
            }
        }
        //生成分享码
        generateShareCodeForUser(null,user);
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
    public boolean addAddress(String userId, String name, String phone, String addr, Boolean isDefault) {
        User user = userDao.findById(userId);
        if (CommonUtil.isNullOrEmpty(user)) {
            return false;
        }
        Address address = new Address();
        address.setUser(user);
        address.setName(name);
        address.setPhone(phone);
        address.setAddress(addr);
        address.setId(CommonUtil.generateUUID32());
        if (isDefault) {
            Address defaultAddr = addressDao.findByUserAndIsDefaultAndDelFlag(user, true,0);
            if (!CommonUtil.isNullOrEmpty(defaultAddr)) {
                defaultAddr.setIsDefault(false);
                addressDao.save(defaultAddr);
            }
            address.setIsDefault(true);
        } else {
            address.setIsDefault(false);
        }
        address.setDelFlag(0);
        addressDao.save(address);
        return true;
    }

    @Transactional
    public boolean deleteAddress(String userId, String addressId) {
        Address address = addressDao.findByIdAndDelFlag(addressId,0);
        if (address == null || !userId.equals(address.getUser().getId())) {
            return false;
        } else {
            address.setDelFlag(1);
            addressDao.save(address);
            return true;
        }
    }

    @Transactional
    public boolean modifyAddress(String userId, String addressId, String name,String phone,String addr,boolean isDefault){
        Address address = addressDao.findByIdAndDelFlag(addressId,0);
        if(address == null || address.getUser() == null || !userId.equals(address.getUser().getId())){
            return false;
        }else{
            address.setName(name);
            address.setPhone(phone);
            address.setAddress(addr);
            if(isDefault){
                Address defaultAddr = addressDao.findByUserAndIsDefaultAndDelFlag(address.getUser(), true,0);
                if (!CommonUtil.isNullOrEmpty(defaultAddr)) {
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
    }

    @Transactional
    public boolean setDefaultAddress(String userId, String addressId) {
        User user = userDao.findById(userId);
        if (CommonUtil.isNullOrEmpty(user)) {
            return false;
        }
        Address address = addressDao.findByIdAndDelFlag(addressId,0);
        if (CommonUtil.isNullOrEmpty(address)) {
            return false;
        }
        Address defaultAddr = addressDao.findByUserAndIsDefaultAndDelFlag(user, true,0);
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
            return new ArrayList<>();
        } else {
            return addressDao.findByUserAndDelFlag(user,0);
        }
    }

    public List<UserVehicleRelationship> getVehicleList(String userId) {
        User user = userDao.findById(userId);
        if (CommonUtil.isNullOrEmpty(user)) {
            return new ArrayList<>();
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
        if (!generateShareCodeForUser(shareCode, user)) {
            return "分享码重复，请更换";
        }
        userDao.save(user);
        return user.getShareCode();
    }

    private boolean generateShareCodeForUser(String shareCode, User user) {
        List<String> shareCodes = userDao.findAllShareCode();
        if(CommonUtil.isNullOrEmpty(shareCode)){
            //生成8位邀请码
            shareCode = CommonUtil.generateUUID32().substring(0,8);
            while (shareCodes.contains(shareCode)){
                shareCode = CommonUtil.generateUUID32().substring(0,8);
            }
        }else{
            if(shareCodes.contains(shareCode)){
                return false;
            }
        }
        user.setShareCode(shareCode);
        return true;
    }

    public boolean queryProduct(String userId, String vehicleId, String queryTitle, String productName, String oemCode, String deliverBy, boolean needReceipt,String address) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        UserQueryProduct userQueryProduct = new UserQueryProduct();
        User user = userDao.findById(userId);
        Vehicle vehicle = vehicleDao.findById(vehicleId);
        if(user == null || vehicle == null){
            return false;
        }
        userQueryProduct.setUser(user);
        userQueryProduct.setVehicle(vehicle);
        userQueryProduct.setQueryTitle(queryTitle);
        userQueryProduct.setProductName(productName);
        userQueryProduct.setOemCode(oemCode);
        userQueryProduct.setDeliverBy(sdf.parse(deliverBy));
        userQueryProduct.setNeedRecipt(needReceipt);
        userQueryProduct.setAddress(address);
        userQueryProduct.setQueryTime(new Date());
        userQueryProductDao.save(userQueryProduct);
        sendQueriedGotSMS(user.getContact());
        return true;
    }

    public Page<UserQueryProduct> getQueriedProducts(String userId, int pageNum, int pageSize){
        User user = userDao.findById(userId);
        Sort sort = new Sort(Sort.Direction.DESC, "queryTime");
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);
        Page<UserQueryProduct> userQueryProducts = userQueryProductDao.findByUser(user, pageable);
        List<UserQueryProduct> userQueryProductList = userQueryProducts.getContent();
        for(UserQueryProduct userQueryProduct : userQueryProductList){
            userQueryProduct.setUser(null);
        }
        return userQueryProducts;
    }

    public boolean addFeedback(String userId,String content,String contact){
        UserFeedback feedback = new UserFeedback();
        User user = userDao.findById(userId);
        if(user==null){
            return false;
        }
        feedback.setUser(user);
        feedback.setContent(content);
        if(!CommonUtil.isNullOrEmpty(contact)){
            feedback.setContact(contact);
        }
        feedback.setTimestamp(new Date());
        userFeedbackDao.save(feedback);
        return true;
    }

    public Page<UserFeedback> getFeedbacks(String userId, int pageNum, int pageSize) {
        User user = userDao.findById(userId);
        Sort sort = new Sort(Sort.Direction.DESC, "timestamp");
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);
        Page<UserFeedback> userFeedbacks = userFeedbackDao.findByUser(user, pageable);
        List<UserFeedback> feedbackList = userFeedbacks.getContent();
        for (UserFeedback feedback : feedbackList) {
            feedback.setUser(null);
        }
        return userFeedbacks;
    }

    public void sendQueriedGotSMS(String phone){
        TaobaoClient client = new DefaultTaobaoClient(ALI_DAYU_URL, ALI_DAYU_APPKEY, ALI_DAYU_SECRET);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("123456");
        req.setSmsType("normal");
        req.setSmsFreeSignName("汽车联盟");
        req.setRecNum(phone);
        req.setSmsTemplateCode("SMS_118730065");
        try {
            AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
            logger.info("----send result : " + rsp.getBody());
            if (rsp == null || rsp.getResult() == null) {
                 logger.error("sendQueriedGotSMS FAIL !");
            }
            if (!rsp.getResult().getSuccess()) {
                logger.error("sendQueriedGotSMS FAIL !");
            }
        } catch (Exception e) {
            logger.error("Send Message Exception: " + e.getMessage());
        }
    }
}
