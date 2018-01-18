package com.nbicc.cu.carsunion.service;

import com.alibaba.fastjson.JSONObject;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.nbicc.cu.carsunion.constant.ParameterValues;
import com.nbicc.cu.carsunion.dao.MHNotifyInfosDao;
import com.nbicc.cu.carsunion.dao.UserDao;
import com.nbicc.cu.carsunion.dao.UserVehicleRelationshipDao;
import com.nbicc.cu.carsunion.dao.VehicleDao;
import com.nbicc.cu.carsunion.model.HostHolder;
import com.nbicc.cu.carsunion.model.MHNotifyInfos;
import com.nbicc.cu.carsunion.model.User;
import com.nbicc.cu.carsunion.model.UserVehicleRelationship;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.MHUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.nbicc.cu.carsunion.constant.ParameterValues.*;

@Service
public class MHService {

    private static Logger logger = LogManager.getLogger(MHService.class);

    @Autowired
    private UserVehicleRelationshipDao userVehicleRelationshipDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private MHNotifyInfosDao mhNotifyInfosDao;

    public List<JSONObject> getVBrand(){
        String vBrandStr = MHUtil.getMHVBrandList();
        Object[] vBrand = JSONObject.parseObject(vBrandStr).getJSONArray("data").toArray();
        List<JSONObject> result = new ArrayList<>();
        for(Object object : vBrand){
            JSONObject tem = JSONObject.parseObject(object.toString());
            tem.remove("attributeNames");
            result.add(tem);
//            if("".equals(tem.getString("peccancyPrice"))){
//                result.add(tem);
//            }
        }
        return result;
    }

    public List<JSONObject> getMHVStyle(String brandId) {
        String vStyleStr = MHUtil.getMHVStyleList(brandId);
        Object[] vStyle = JSONObject.parseObject(vStyleStr).getJSONArray("data").toArray();
        List<JSONObject> result = new ArrayList<>();
        for(Object object : vStyle){
            JSONObject tem = JSONObject.parseObject(object.toString());
            tem.remove("attributeNames");
            result.add(tem);
        }
        return result;
    }


    public List<JSONObject> getMHVModelList(String styleId) {
        String vModelStr = MHUtil.getMHVModelList(styleId);
        Object[] vModel = JSONObject.parseObject(vModelStr).getJSONArray("data").toArray();
        List<JSONObject> result = new ArrayList<>();
        for(Object object : vModel){
            JSONObject tem = JSONObject.parseObject(object.toString());
            tem.remove("attributeNames");
            result.add(tem);
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject bindVehicle(String userId, String plateNum, String vin, String brandId, String styleId, String modelId, String engineNum, String equipmentCode, String purchaseDate, String brandStr, String styleStr, String modelStr){
        User user = userDao.findById(userId);
        if(CommonUtil.isNullOrEmpty(user)){
            return null;
        }
        UserVehicleRelationship uvr = userVehicleRelationshipDao.findByUserAndIsDefault(user, true);
        if(CommonUtil.isNullOrEmpty(uvr)){
            return null;
        }

        String result = MHUtil.addMHVehicle(plateNum, vin, brandId, styleId, modelId, ParameterValues.MH_GROUPCODE, engineNum, equipmentCode, purchaseDate);
        JSONObject json = JSONObject.parseObject(result);
        if ("0".equals(json.getString("errno"))) {
            String mh_vehicle_id = json.getString("id");
            String mh_hw_id = json.getString("hwId");
            uvr.setIsBindMh(true);
            uvr.setMhVehicleId(mh_vehicle_id);
            uvr.setMhHwId(mh_hw_id);
            uvr.setMhBrand(brandStr);
            uvr.setMhStyle(styleStr);
            uvr.setMhModel(modelStr);
            userVehicleRelationshipDao.save(uvr);
        }
        logger.info("bindVehicle result : " + result);
        return json;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean unbindVehicle(String userId){
        User user = userDao.findById(userId);
        UserVehicleRelationship uvr = userVehicleRelationshipDao.findByUserAndIsDefault(user,true);
        if(uvr == null || !uvr.getIsBindMh() || CommonUtil.isNullOrEmpty(uvr.getMhVehicleId())){
            return false;
        }else{
            String result = MHUtil.deleteMHVehicle(uvr.getMhVehicleId());
            JSONObject json = JSONObject.parseObject(result);
            logger.info("unbindVehicle result : " + result);
            if("0".equals(json.getString("errno"))){
                uvr.setMhHwId(null);
                uvr.setMhVehicleId(null);
                uvr.setIsBindMh(false);
                uvr.setMhBrand(null);
                uvr.setMhStyle(null);
                uvr.setMhModel(null);
                userVehicleRelationshipDao.save(uvr);
                return true;
            }else{
                return false;
            }
        }
    }

    public JSONObject updateVehicle(String id, String plateNum, String vin, String brandId, String styleId, String modelId, String engineNum, String purchaseDate, String brandStr, String styleStr, String modelStr) {
        User user = userDao.findById(hostHolder.getAdmin().getId());
        if(CommonUtil.isNullOrEmpty(user)){
            return null;
        }
        UserVehicleRelationship uvr = userVehicleRelationshipDao.findByUserAndIsDefault(user, true);
        if(CommonUtil.isNullOrEmpty(uvr)){
            return null;
        }
        uvr.setMhModel(modelStr);
        uvr.setMhStyle(styleStr);
        uvr.setMhBrand(brandStr);
        userVehicleRelationshipDao.save(uvr);
        String result = MHUtil.updateMHVehicle(id, plateNum, vin, brandId, styleId, modelId, engineNum, purchaseDate);
        logger.info("updateVehicle result : " + result);
        return JSONObject.parseObject(result);
    }

    public JSONObject getMHVehicleStatus(String hwId) {
        String statusStr = MHUtil.getMHVehicleStatus(hwId);
        logger.info("getMHVehicleStatus result : " + statusStr);
        if("".equals(statusStr)) {
            return null;
        }
        return JSONObject.parseObject(statusStr);
    }

    public JSONObject getMHVehiclePosition(String hwId) {
        String positionStr = MHUtil.getMHVehiclePosition(hwId);
        logger.info("getMHVehiclePosition result : " + positionStr);
        if("".equals(positionStr)) {
            return null;
        }
        return JSONObject.parseObject(positionStr);
    }

    public JSONObject getMHVehicleDetails(String id) {
        String ret = MHUtil.getMHVehicleDetails(id);
        logger.info("getMHVehicleDetails result : " + ret);
        if("".equals(ret)) {
            return null;
        }
        return JSONObject.parseObject(ret);
    }

    public JSONObject controlMHVehicle(String hwId, String unFireTime, String key, int value) {
        String ret = MHUtil.controlMHVehicle(hwId,unFireTime,key,value);
        logger.info("controlMHVehicle result : " + ret + " || hwId : " + hwId + " key:value : " + key + ":" + value);
        if("".equals(ret)) {
            return null;
        }
        return JSONObject.parseObject(ret);
    }

    public UserVehicleRelationship getDefaultMHDevice(String userId) {
        return userVehicleRelationshipDao.findByUserAndIsDefault(userDao.findById(userId),true);
    }

    public JSONObject vehicleSetting(String hwId, String key, String value){
        String ret = MHUtil.MHVehicleSetting(hwId,key,value);
        logger.info("vehicleSetting result : " + ret + " || hwId : " + hwId + " key:value : " + key + ":" + value);
        if("".equals(ret)) {
            return null;
        }
        return JSONObject.parseObject(ret);
    }

    public Page<MHNotifyInfos> getMHNotifyInfosList(String userId, int pageNum, int pageSize){
        Sort sort = new Sort(Sort.Direction.DESC,"time");
        Pageable pageable = new PageRequest(pageNum,pageSize,sort);
        Page<MHNotifyInfos> list = mhNotifyInfosDao.findByUserId(userId,pageable);
        list.getContent().stream().forEach(MHNotifyInfos::fillContent);
        return list;
    }


    public void handlerNotify(MHNotifyInfos info) {
        logger.info("Receive MH notify : " + info.toString());
        UserVehicleRelationship uvr = userVehicleRelationshipDao.findByMhVehicleId(info.getObject());
        if(uvr == null){
            logger.info("mh_id in notify can not find ! receive mh_id is " + info.getObject());
            mhNotifyInfosDao.save(info);
            return;
        }
        info.setUserId(uvr.getUser().getId());
        info.setPlateNum(uvr.getPlateNum());
        mhNotifyInfosDao.save(info);
        String cid = uvr.getUser().getPushCid();
        // todo 先写死，所有通知推到我的app上
//        cid = "1ce80dd956763442d97f797e1f7b1006";
        info.fillContent();
        String plateNum = info.getPlateNum();
        if("1".equals(info.getType())){
            //告警消息
            String title = "告警通知";
            String body = "您的汽车（" + plateNum + ")有告警：" + info.getSubTypeContent();
            sendNotify2App(cid,title,body,info);
        }
    }

    public boolean sendNotify2App(String cid, String title, String body, MHNotifyInfos info){
        IGtPush push = new IGtPush(GETUI_URL, GETUI_APP_KEY_DEV, GETUI_MASTER_SECRET_DEV);
        TransmissionTemplate template = getTemplate(title,body,info);
        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(template);
        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
        message.setPushNetWorkType(0);
        Target target = new Target();
        target.setAppId(GETUI_APP_ID_DEV);
        target.setClientId(cid);
        //target.setAlias(Alias);
        IPushResult ret = null;
        try {
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            logger.error("GETUI PUSH ERROR : " + e.getMessage());
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
//            logger.info("GETUI PUSH RESULT : " + ret.getResponse().toString());
            return true;
        } else {
            logger.error("服务器响应异常");
            return false;
        }


    }

    public TransmissionTemplate getTemplate(String title, String body,MHNotifyInfos info) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(GETUI_APP_ID_DEV);
        template.setAppkey(GETUI_APP_KEY_DEV);
        String content = JSONObject.toJSONString(info);
        template.setTransmissionContent(content);
//        System.out.println(info.toString());
        template.setTransmissionType(2);
        APNPayload payload = new APNPayload();
        //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
        payload.setAutoBadge("+1");
        payload.setContentAvailable(1);
//        payload.setSound("default");
//        payload.setCategory("$category");
        payload.addCustomMsg("myTransmissionContent",content);

        //字典模式使用APNPayload.DictionaryAlertMsg
        payload.setAlertMsg(getDictionaryAlertMsg(title,body));
        template.setAPNInfo(payload);
        return template;
    }

    private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(String title, String body){
        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
        alertMsg.setBody(body);
//        alertMsg.setActionLocKey("ActionLockey");
//        alertMsg.setLocKey("LocKey");
//        alertMsg.addLocArg("loc-args");
//        alertMsg.setLaunchImage("launch-image");
        // iOS8.2以上版本支持
        alertMsg.setTitle(title);
//        alertMsg.setTitleLocKey("TitleLocKey");
//        alertMsg.addTitleLocArg("TitleLocArg");
        return alertMsg;
    }
}
