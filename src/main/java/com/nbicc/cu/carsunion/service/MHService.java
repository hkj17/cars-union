package com.nbicc.cu.carsunion.service;

import com.alibaba.fastjson.JSONObject;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import com.nbicc.cu.carsunion.constant.ParameterValues;
import com.nbicc.cu.carsunion.dao.MHNotifyInfosDao;
import com.nbicc.cu.carsunion.dao.UserDao;
import com.nbicc.cu.carsunion.dao.UserVehicleRelationshipDao;
import com.nbicc.cu.carsunion.model.HostHolder;
import com.nbicc.cu.carsunion.model.MHNotifyInfos;
import com.nbicc.cu.carsunion.model.User;
import com.nbicc.cu.carsunion.model.UserVehicleRelationship;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.MHUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void handlerNotify(MHNotifyInfos info) {
        logger.info("Receive MH notify : " + info.toString());
        mhNotifyInfosDao.save(info);
    }

    public boolean sendNotify2App(String cid){
        IGtPush push = new IGtPush(GETUI_URL, GETUI_APP_KEY_DEV, GETUI_MASTER_SECRET_DEV);
        LinkTemplate template = linkTemplateDemo();
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
            logger.info("GETUI PUSH RESULT : " + ret.getResponse().toString());
            return true;
        } else {
            logger.error("服务器响应异常");
            return false;
        }


    }

    private LinkTemplate linkTemplateDemo() {
        LinkTemplate template = new LinkTemplate();
        // 设置APPID与APPKEY
        template.setAppId(GETUI_APP_ID_DEV);
        template.setAppkey(GETUI_APP_KEY_DEV);

        Style0 style = new Style0();
        // 设置通知栏标题与内容
        style.setTitle("汽车联盟通知栏标题");
        style.setText("汽车联盟通知栏内容");
        // 配置通知栏图标
        style.setLogo("icon.png");
        // 配置通知栏网络图标
        style.setLogoUrl("");
        // 设置通知是否响铃，震动，或者可清除
        style.setRing(true);
        style.setVibrate(true);
        style.setClearable(true);
        template.setStyle(style);

        // 设置打开的网址地址
        template.setUrl("http://www.iot-jd.com/");
        return template;
    }
}
