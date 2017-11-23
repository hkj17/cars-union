package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.Authority;
import com.nbicc.cu.carsunion.constant.ParameterValues;
import com.nbicc.cu.carsunion.enumtype.ResponseType;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.MHUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/control")
@Authority
public class MHController {

    @RequestMapping(value = "getVBrand", method = RequestMethod.GET)
    public JSONObject getVBrand(){
        String vBrand = MHUtil.getMHVBrandList();
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",vBrand);
    }

    @RequestMapping(value = "getVStyle", method = RequestMethod.POST)
    public JSONObject getVStyle(@RequestParam(value = "brandId") String brandId){
        String vStyle = MHUtil.getMHVStyleList(brandId);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",vStyle);
    }

    @RequestMapping(value = "getVModel", method = RequestMethod.POST)
    public JSONObject getVModel(@RequestParam(value = "styleId") String styleId){
        String vModel = MHUtil.getMHVModelList(styleId);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",vModel);
    }

    @RequestMapping(value = "bindVehicle", method = RequestMethod.POST)
    public JSONObject bindVehicle(@RequestParam(value = "plateNum") String plateNum,
                                  @RequestParam(value = "vin") String vin,
                                  @RequestParam(value = "brandId") String brandId,
                                  @RequestParam(value = "styleId") String styleId,
                                  @RequestParam(value = "modelId") String modelId,
                                  @RequestParam(value = "groupCode") String groupCode,
                                  @RequestParam(value = "engineNum") String engineNum,
                                  @RequestParam(value = "equipmentCode") String equipmentCode,
                                  @RequestParam(value = "purchaseDate") String purchaseDate){
        String ret = MHUtil.addMHVehicle(plateNum,vin,brandId,styleId,modelId,groupCode,engineNum,equipmentCode,purchaseDate);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",ret);
    }

    @RequestMapping(value = "updateVehicle", method = RequestMethod.POST)
    public JSONObject updateVehicle(@RequestParam(value = "id") String id,
                                  @RequestParam(value = "plateNum") String plateNum,
                                  @RequestParam(value = "vin") String vin,
                                  @RequestParam(value = "brandId") String brandId,
                                  @RequestParam(value = "styleId") String styleId,
                                  @RequestParam(value = "modelId") String modelId,
                                  @RequestParam(value = "engineNum") String engineNum,
                                  @RequestParam(value = "purchaseDate") String purchaseDate){
        String ret = MHUtil.updateMHVehicle(id,plateNum,vin,brandId,styleId,modelId,engineNum,purchaseDate);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",ret);
    }

    @RequestMapping(value = "getVehicleDetails", method = RequestMethod.POST)
    public JSONObject getVehicleDetails(@RequestParam(value = "id") String id){
        String ret = MHUtil.getMHVehicleDetails(id);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",ret);
    }

    @RequestMapping(value = "unbindVehicle", method = RequestMethod.POST)
    public JSONObject unbindVehicle(@RequestParam(value = "id") String id){
        String ret = MHUtil.deleteMHVehicle(id);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",ret);
    }

    @RequestMapping(value = "controlVehicle", method = RequestMethod.POST)
    public JSONObject controlVehicle(@RequestParam(value = "hwId") String hwId,
                                     @RequestParam(value = "key") String key,
                                     @RequestParam(value = "value") int value){
        String ret = MHUtil.controlMHVehicle(hwId,key,value);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",ret);
    }

    @RequestMapping(value = "getVehiclePosition", method = RequestMethod.POST)
    public JSONObject getVehiclePosition(@RequestParam(value = "hwId") String hwId){
        String position = MHUtil.getMHVehiclePosition(hwId);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",position);
    }

    @RequestMapping(value = "getVehicleStatus", method = RequestMethod.POST)
    public JSONObject getVehicleStatus(@RequestParam(value = "hwId") String hwId){
        String status = MHUtil.getMHVehicleStatus(hwId);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",status);
    }
}
