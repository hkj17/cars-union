package com.nbicc.cu.carsunion.service;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.dao.UserDao;
import com.nbicc.cu.carsunion.dao.UserVehicleRelationshipDao;
import com.nbicc.cu.carsunion.model.UserVehicleRelationship;
import com.nbicc.cu.carsunion.util.MHUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
