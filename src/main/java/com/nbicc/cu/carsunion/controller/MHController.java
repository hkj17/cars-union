package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.Authority;
import com.nbicc.cu.carsunion.constant.AuthorityType;
import com.nbicc.cu.carsunion.enumtype.ResponseType;
import com.nbicc.cu.carsunion.model.HostHolder;
import com.nbicc.cu.carsunion.service.MHService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.MHUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/control")
@Authority(value = AuthorityType.UserValidate)
public class MHController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    private MHService mhService;

    @RequestMapping(value = "getVBrand", method = RequestMethod.GET)
    public JSONObject getVBrand(){
        List<JSONObject> vBrand = mhService.getVBrand();
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",vBrand);
    }

    @RequestMapping(value = "getVStyle", method = RequestMethod.POST)
    public JSONObject getVStyle(@RequestParam(value = "brandId") String brandId){
        List<JSONObject> vStyle = mhService.getMHVStyle(brandId);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",vStyle);
    }

    @RequestMapping(value = "getVModel", method = RequestMethod.POST)
    public JSONObject getVModel(@RequestParam(value = "styleId") String styleId){
        List<JSONObject> vModel = mhService.getMHVModelList(styleId);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",vModel);
    }

    @RequestMapping(value = "bindVehicle", method = RequestMethod.POST)
    public JSONObject bindVehicle(@RequestParam(value = "plateNum") String plateNum,
                                  @RequestParam(value = "vin") String vin,
                                  @RequestParam(value = "brandId") String brandId,
                                  @RequestParam(value = "styleId") String styleId,
                                  @RequestParam(value = "modelId") String modelId,
                                  @RequestParam(value = "engineNum") String engineNum,
                                  @RequestParam(value = "equipmentCode") String equipmentCode,
                                  @RequestParam(value = "purchaseDate") String purchaseDate){
        String userId = hostHolder.getAdmin().getId();
        JSONObject json = mhService.bindVehicle(userId, plateNum, vin, brandId, styleId, modelId, engineNum, equipmentCode, purchaseDate);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "返回成功", json);

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
        JSONObject result = mhService.updateVehicle(id,plateNum,vin,brandId,styleId,modelId,engineNum,purchaseDate);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",result);
    }

    @RequestMapping(value = "getVehicleDetails", method = RequestMethod.POST)
    public JSONObject getVehicleDetails(@RequestParam(value = "id") String id){
        String ret = MHUtil.getMHVehicleDetails(id);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",ret);
    }

    @RequestMapping(value = "unbindVehicle", method = RequestMethod.POST)
    public JSONObject unbindVehicle(){
        String userId = hostHolder.getAdmin().getId();
        boolean state = mhService.unbindVehicle(userId);
        if(state){
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "操作失败",null);
        }
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
