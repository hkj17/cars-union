package com.nbicc.cu.carsunion.util;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.http.MHHttpRequest;

public class MHUtil {

    //3.1查询品牌
    public static String getMHVBrandList(){
        MHHttpRequest request = new MHHttpRequest("http://117.78.36.98/web/tpc/api/brands");
        return request.getResponseGET();
    }

    //3.2查询车系
    public static String getMHVStyleList(String brandId){
        MHHttpRequest request = new MHHttpRequest("http://117.78.36.98/web/tpc/api/styles");
        request.setParameter("brandId",brandId);
        return request.getResponseGET();
    }

    //3.3查询车型
    public static String getMHVModelList(String styleId){
        MHHttpRequest request = new MHHttpRequest("http://117.78.36.98/web/tpc/api/models");
        request.setParameter("styleId",styleId);
        return request.getResponseGET();
    }

    //3.4新增车辆
    public static String addMHVehicle(String plateNumber,String vin,String brandId,String styleId,String modelId,String groupCode,String engineNumber,String equipmentCode,String purchaseDate){
        MHHttpRequest request = new MHHttpRequest("http://117.78.36.98/web/tpc/api/vehicle/add");
        JSONObject json = new JSONObject();
        if(!CommonUtil.isNullOrEmpty(plateNumber))
            json.put("plateNumber",plateNumber);
        if(!CommonUtil.isNullOrEmpty(vin))
            json.put("vin",vin);
        if(!CommonUtil.isNullOrEmpty(brandId))
            json.put("brand_id",brandId);
        if(!CommonUtil.isNullOrEmpty(styleId))
            json.put("style_id",styleId);
        if(!CommonUtil.isNullOrEmpty(modelId))
            json.put("model_id",modelId);
        //必填
        json.put("groupCode",groupCode);
        if(!CommonUtil.isNullOrEmpty(engineNumber))
            json.put("engineNumber",engineNumber);
        //必填
        json.put("equipmentCode",equipmentCode);
        if(!CommonUtil.isNullOrEmpty(purchaseDate))
            json.put("purchaseDate",purchaseDate);
        return request.getResponsePOST(json.toString());
    }

    //3.5修改车辆
    public static String updateMHVehicle(String id, String plateNumber,String vin,String brandId,String styleId,String modelId,String engineNumber,String purchaseDate){
        MHHttpRequest request = new MHHttpRequest("http://117.78.36.98/web/tpc/api/vehicle/update");
        JSONObject json = new JSONObject();
        request.setParameter("vehicleId",id);
        if(!CommonUtil.isNullOrEmpty(plateNumber))
            json.put("plateNumber",plateNumber);
        if(!CommonUtil.isNullOrEmpty(vin))
            json.put("vin",vin);
        if(!CommonUtil.isNullOrEmpty(brandId))
            json.put("brand_id",brandId);
        if(!CommonUtil.isNullOrEmpty(styleId))
            json.put("style_id",styleId);
        if(!CommonUtil.isNullOrEmpty(modelId))
            json.put("model_id",modelId);
        if(!CommonUtil.isNullOrEmpty(engineNumber))
            json.put("engineNumber",engineNumber);
        if(!CommonUtil.isNullOrEmpty(purchaseDate))
            json.put("purchaseDate",purchaseDate);
        return request.getResponsePOST(json.toString());
    }

    //3.6查询车辆
    public static String getMHVehicleDetails(String id){
        MHHttpRequest request = new MHHttpRequest("http://117.78.36.98/web/tpc/api/vehicle/searchForSingle");
        request.setParameter("vehicleId",id);
        return request.getResponseGET();
    }

    //3.9车辆定位
    public static String getMHVehiclePosition(String hwId){
        MHHttpRequest request = new MHHttpRequest("http://117.78.36.98/web/tpc/api/vehicle/positions");
        request.setParameter("vehicleHwid",hwId);
        return request.getResponseGET();
    }

    //3.10查询车身状态
    public static String getMHVehicleStatus(String hwId){
        MHHttpRequest request = new MHHttpRequest("http://117.78.36.98/web/tpc/api/vehicle/status");
        request.setParameter("vehicleHwid",hwId);
        return request.getResponseGET();
    }

    //3.13告警类型查询
    public static String getMHAlertTypeList(){
        MHHttpRequest request = new MHHttpRequest("http://117.78.36.98/web/tpc/api/vehicle/alerts/types");
        return request.getResponseGET();
    }
}
