package com.nbicc.cu.carsunion.service;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.ParameterValues;
import com.nbicc.cu.carsunion.dao.UserDao;
import com.nbicc.cu.carsunion.dao.UserVehicleRelationshipDao;
import com.nbicc.cu.carsunion.model.User;
import com.nbicc.cu.carsunion.model.UserVehicleRelationship;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.MHUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MHService {

    @Autowired
    private UserVehicleRelationshipDao userVehicleRelationshipDao;
    @Autowired
    private UserDao userDao;

    public List<JSONObject> getVBrand(){
        String vBrandStr = MHUtil.getMHVBrandList();
        Object[] vBrand = JSONObject.parseObject(vBrandStr).getJSONArray("data").toArray();
        List<JSONObject> result = new ArrayList<>();
        for(Object object : vBrand){
            JSONObject tem = JSONObject.parseObject(object.toString());
            tem.remove("attributeNames");
            if("".equals(tem.getString("peccancyPrice"))){
                result.add(tem);
            }
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

    @Transactional
    public JSONObject bindVehicle(String userId, String plateNum, String vin, String brandId, String styleId, String modelId, String engineNum, String equipmentCode, String purchaseDate){
        try {
            String result = MHUtil.addMHVehicle(plateNum, vin, brandId, styleId, modelId, ParameterValues.MH_GROUPCODE, engineNum, equipmentCode, purchaseDate);
            System.out.println(result);
            JSONObject json = JSONObject.parseObject(result);
            if ("0".equals(json.getString("errno"))) {
                String mh_vehicle_id = json.getString("id");
                String mh_hw_id = json.getString("hwId");
                User user = userDao.findById(userId);
                UserVehicleRelationship uvr = userVehicleRelationshipDao.findByUserAndIsDefault(user, true);
                uvr.setIsBindMh(true);
                uvr.setMhVehicleId(mh_vehicle_id);
                uvr.setMhHwId(mh_hw_id);
                userVehicleRelationshipDao.save(uvr);
            }
            return json;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public boolean unbindVehicle(String userId){
        User user = userDao.findById(userId);
        UserVehicleRelationship uvr = userVehicleRelationshipDao.findByUserAndIsDefault(user,true);
        if(uvr == null || !uvr.getIsBindMh() || CommonUtil.isNullOrEmpty(uvr.getMhVehicleId())){
            return false;
        }else{
            String result = MHUtil.deleteMHVehicle(uvr.getMhVehicleId());
            System.out.println(result);
            JSONObject json = JSONObject.parseObject(result);
            if("0".equals(json.getString("errno"))){
                uvr.setMhHwId(null);
                uvr.setMhVehicleId(null);
                uvr.setIsBindMh(false);
                userVehicleRelationshipDao.save(uvr);
                return true;
            }else{
                return false;
            }
        }
    }

    public JSONObject updateVehicle(String id, String plateNum, String vin, String brandId, String styleId, String modelId, String engineNum, String purchaseDate) {
        String result = MHUtil.updateMHVehicle(id, plateNum, vin, brandId, styleId, modelId, engineNum, purchaseDate);
        return JSONObject.parseObject(result);
    }

    public Object[] getMHVehicleStatus(String hwId) {
        String statusStr = MHUtil.getMHVehicleStatus(hwId);
        return JSONObject.parseObject(statusStr).getJSONArray("data").toArray();

    }

    public JSONObject getMHVehiclePosition(String hwId) {
        String positionStr = MHUtil.getMHVehiclePosition(hwId);
        return JSONObject.parseObject(positionStr).getJSONObject("data");
    }

    public JSONObject getMHVehicleDetails(String id) {
        String ret = MHUtil.getMHVehicleDetails(id);
        return JSONObject.parseObject(ret).getJSONObject("data");
    }

    public JSONObject controlMHVehicle(String hwId, String key, int value) {
        String ret = MHUtil.controlMHVehicle(hwId,key,value);
        return JSONObject.parseObject(ret);
    }

    public UserVehicleRelationship getDefaultMHDevice(String userId) {
        return userVehicleRelationshipDao.findByUserAndIsDefault(userDao.findById(userId),true);
    }
}
