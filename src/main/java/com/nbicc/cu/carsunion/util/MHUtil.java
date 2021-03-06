package com.nbicc.cu.carsunion.util;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.ParameterValues;
import com.nbicc.cu.carsunion.http.MHHttpRequest;

import java.util.Iterator;
import java.util.List;

public class MHUtil {

    //3.1查询品牌
    public static String getMHVBrandList(){
        MHHttpRequest request = new MHHttpRequest(ParameterValues.MH_GATEWAY_URL + "/tpc/api/brands");
        return request.getResponseGET();
    }

    //3.2查询车系
    public static String getMHVStyleList(String brandId){
        MHHttpRequest request = new MHHttpRequest(ParameterValues.MH_GATEWAY_URL + "/tpc/api/styles");
        request.setParameter("brandId",brandId);
        return request.getResponseGET();
    }

    //3.3查询车型
    public static String getMHVModelList(String styleId){
        MHHttpRequest request = new MHHttpRequest(ParameterValues.MH_GATEWAY_URL + "/tpc/api/models");
        request.setParameter("styleId",styleId);
        return request.getResponseGET();
    }

    //3.4新增车辆
    public static String addMHVehicle(String plateNumber,String vin,String brandId,String styleId,String modelId,String groupCode,String engineNumber,String equipmentCode,String purchaseDate){
        MHHttpRequest request = new MHHttpRequest(ParameterValues.MH_GATEWAY_URL + "/tpc/api/vehicle/add");
        JSONObject json = new JSONObject();
        if(!CommonUtil.isNullOrEmpty(plateNumber)) {
            json.put("plateNumber", plateNumber);
        }
        if(!CommonUtil.isNullOrEmpty(vin)) {
            json.put("vin", vin);
        }
        if(!CommonUtil.isNullOrEmpty(brandId)) {
            json.put("brand_id", brandId);
        }
        if(!CommonUtil.isNullOrEmpty(styleId)) {
            json.put("style_id", styleId);
        }
        if(!CommonUtil.isNullOrEmpty(modelId)) {
            json.put("model_id", modelId);
        }
        //必填
        json.put("groupCode",groupCode);
        if(!CommonUtil.isNullOrEmpty(engineNumber)) {
            json.put("engineNumber", engineNumber);
        }
        //必填
        json.put("equipmentCode",equipmentCode);
        if(!CommonUtil.isNullOrEmpty(purchaseDate)) {
            json.put("purchaseDate", purchaseDate);
        }
        return request.getResponsePOST(json.toString());
    }

    //3.5修改车辆
    public static String updateMHVehicle(String id, String plateNumber,String vin,String brandId,String styleId,String modelId,String engineNumber,String purchaseDate){
        MHHttpRequest request = new MHHttpRequest(ParameterValues.MH_GATEWAY_URL + "/tpc/api/vehicle/update");
        JSONObject json = new JSONObject();
        request.setParameter("vehicleId",id);
        if(!CommonUtil.isNullOrEmpty(plateNumber)) {
            json.put("plateNumber", plateNumber);
        }
        if(!CommonUtil.isNullOrEmpty(vin)) {
            json.put("vin", vin);
        }
        if(!CommonUtil.isNullOrEmpty(brandId)) {
            json.put("brand_id", brandId);
        }
        if(!CommonUtil.isNullOrEmpty(styleId)) {
            json.put("style_id", styleId);
        }
        if(!CommonUtil.isNullOrEmpty(modelId)) {
            json.put("model_id", modelId);
        }
        if(!CommonUtil.isNullOrEmpty(engineNumber)) {
            json.put("engineNumber", engineNumber);
        }
        if(!CommonUtil.isNullOrEmpty(purchaseDate)) {
            json.put("purchaseDate", purchaseDate);
        }
        return request.getResponsePOST(json.toString());
    }

    //3.6查询车辆
    public static String getMHVehicleDetails(String id){
        MHHttpRequest request = new MHHttpRequest(ParameterValues.MH_GATEWAY_URL + "/tpc/api/vehicle/searchForSingle");
        request.setParameter("vehicleId",id);
        return request.getResponseGET();
    }

    //3.7删除车辆
    public static String deleteMHVehicle(String id){
        MHHttpRequest request = new MHHttpRequest(ParameterValues.MH_GATEWAY_URL + "/tpc/api/vehicle/delete");
        request.setParameter("vehicleId",id);
        return request.getResponseGET();
    }

    //3.8车辆控制
    public static String controlMHVehicle(String hwId,String unFireTime, String key,int value){
        MHHttpRequest request = new MHHttpRequest(ParameterValues.MH_GATEWAY_URL + "/tpc/api/vehicle/controls");
        JSONObject json = new JSONObject();
        JSONObject command = new JSONObject();
        command.put(key,value);
        json.put("vehicleHwid",hwId);
        json.put("unFireTime",unFireTime);
        json.put("order",command);
        return request.getResponsePOST(json.toString());
    }

    //3.9车辆定位
    public static String getMHVehiclePosition(String hwId){
        MHHttpRequest request = new MHHttpRequest(ParameterValues.MH_GATEWAY_URL + "/tpc/api/vehicle/positions");
        request.setParameter("vehicleHwid",hwId);
        return request.getResponseGET();
    }

    //3.10查询车身状态
    public static String getMHVehicleStatus(String hwId){
        MHHttpRequest request = new MHHttpRequest(ParameterValues.MH_GATEWAY_URL + "/tpc/api/vehicle/status");
        request.setParameter("vehicleHwid",hwId);
        return request.getResponseGET();
    }

    //TODO: 报错
    //3.11行程查询
    public static String getMHVehicleTripList(String hwId,String start,String end){
        MHHttpRequest request = new MHHttpRequest(ParameterValues.MH_GATEWAY_URL + "/tpc/api/vehicle/trips");
        request.setParameter("vehicleHwid",hwId);
        request.setParameter("date_from",start);
        request.setParameter("date_to",end);
        return request.getResponseGET();
    }

    //3.13告警类型查询
    public static String getMHAlertTypeList(){
        MHHttpRequest request = new MHHttpRequest(ParameterValues.MH_GATEWAY_URL + "/tpc/api/vehicle/alerts/types");
        return request.getResponseGET();
    }

    //3.15查询车辆参数
    public static String getMHVehicleSetting(String hwId, List<String> params){
        MHHttpRequest request = new MHHttpRequest(ParameterValues.MH_GATEWAY_URL + "/tpc/api/vehicle/searchSetting");
        request.setParameter("vehicleHwid",hwId);
        StringBuilder sb = new StringBuilder();
        Iterator<String> iter = params.iterator();
        if(iter.hasNext()){
            sb.append(iter.next());
        }
        while(iter.hasNext()){
            sb.append(",").append(iter.next());
        }
        request.setParameter("params",sb.toString());
        return request.getResponseGET();
    }

    //3.16
    public static String MHVehicleSetting(String hwId,String key,String value){
        MHHttpRequest request = new MHHttpRequest(ParameterValues.MH_GATEWAY_URL + "/tpc/api/vehicles/settings");
        JSONObject command = new JSONObject();
        if(!"dialSwitch".equals(key)){
            command.put(key,Integer.parseInt(value));
        }else {
            command.put(key, value);
        }
        request.setParameter("vehicleHwid",hwId);
        request.setParameter("jsonStr",command.toString());
        return request.getResponsePOST(command.toString());
    }

}
