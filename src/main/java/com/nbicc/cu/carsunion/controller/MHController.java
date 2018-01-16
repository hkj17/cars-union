package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.Authority;
import com.nbicc.cu.carsunion.constant.AuthorityType;
import com.nbicc.cu.carsunion.enumtype.ResponseType;
import com.nbicc.cu.carsunion.model.HostHolder;
import com.nbicc.cu.carsunion.model.MHNotifyInfos;
import com.nbicc.cu.carsunion.model.UserVehicleRelationship;
import com.nbicc.cu.carsunion.service.MHService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/control")
@Authority(value = AuthorityType.UserValidate)
public class MHController {

    @Autowired
    private MHService mhService;
    @Autowired
    private HostHolder hostHolder;

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
                                  @RequestParam(value = "brandStr") String brandStr,
                                  @RequestParam(value = "styleStr") String styleStr,
                                  @RequestParam(value = "modelStr") String modelStr,
                                  @RequestParam(value = "engineNum") String engineNum,
                                  @RequestParam(value = "equipmentCode") String equipmentCode,
                                  @RequestParam(value = "purchaseDate") String purchaseDate){
        String userId = hostHolder.getAdmin().getId();
        JSONObject json = mhService.bindVehicle(userId, plateNum, vin, brandId, styleId, modelId, engineNum, equipmentCode, purchaseDate,brandStr,styleStr,modelStr);
        if(json == null){
            return CommonUtil.response(ResponseType.REQUEST_FAIL,"用户未选择默认车型",null);
        }else {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "返回成功", json);
        }
    }

    @RequestMapping(value = "updateVehicle", method = RequestMethod.POST)
    public JSONObject updateVehicle(@RequestParam(value = "id") String id,
                                  @RequestParam(value = "plateNum") String plateNum,
                                  @RequestParam(value = "vin") String vin,
                                  @RequestParam(value = "brandId") String brandId,
                                  @RequestParam(value = "styleId") String styleId,
                                  @RequestParam(value = "modelId") String modelId,
                                    @RequestParam(value = "brandStr") String brandStr,
                                    @RequestParam(value = "styleStr") String styleStr,
                                    @RequestParam(value = "modelStr") String modelStr,
                                  @RequestParam(value = "engineNum") String engineNum,
                                  @RequestParam(value = "purchaseDate") String purchaseDate){
        JSONObject result = mhService.updateVehicle(id,plateNum,vin,brandId,styleId,modelId,engineNum,purchaseDate,brandStr,styleStr,modelStr);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",result);
    }

    @RequestMapping(value = "getVehicleDetails", method = RequestMethod.GET)
    public JSONObject getVehicleDetails(){
        UserVehicleRelationship relationship = mhService.getDefaultMHDevice(hostHolder.getAdmin().getId());
        if(relationship == null || !relationship.getIsBindMh()){
            return CommonUtil.response(ResponseType.NOT_BIND_MH,"未绑定MH设备",null);
        }
        String id = relationship.getMhVehicleId();
        JSONObject ret = mhService.getMHVehicleDetails(id);
        if(ret != null && "0".equals(ret.getString("errno"))){
            JSONObject result = ret.getJSONObject("data");
            result.put("brandStr",relationship.getMhBrand());
            result.put("styleStr",relationship.getMhStyle());
            result.put("modelStr",relationship.getMhModel());
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",result);
        }else{
            return CommonUtil.response(ResponseType.REQUEST_FAIL,"请求失败:错误的设备信息或服务器错误",null);
        }
    }


    @RequestMapping(value = "unbindVehicle", method = RequestMethod.POST)
    public JSONObject unbindVehicle() {
        String userId = hostHolder.getAdmin().getId();
        boolean state = mhService.unbindVehicle(userId);
        if (state) {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功", null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "操作失败", null);
        }
    }

    @RequestMapping(value = "controlVehicle", method = RequestMethod.POST)
    public JSONObject controlVehicle(@RequestParam(value = "unFire",defaultValue = "0") String unfire,
                                     @RequestParam(value = "key") String key,
                                     @RequestParam(value = "value") int value){
        UserVehicleRelationship relationship = mhService.getDefaultMHDevice(hostHolder.getAdmin().getId());
        if(relationship == null || !relationship.getIsBindMh()){
            return CommonUtil.response(ResponseType.NOT_BIND_MH,"未绑定MH设备",null);
        }
        String hwId = relationship.getMhHwId();
        JSONObject ret = mhService.controlMHVehicle(hwId,unfire,key,value);
        if(ret != null && "0".equals(ret.getString("errno"))){
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",ret.getJSONObject("data"));
        }else{
            return CommonUtil.response(ResponseType.REQUEST_FAIL,"请求失败:错误的设备信息或服务器错误",null);
        }
    }

    @RequestMapping(value = "getVehiclePosition", method = RequestMethod.GET)
    public JSONObject getVehiclePosition(){
        UserVehicleRelationship relationship = mhService.getDefaultMHDevice(hostHolder.getAdmin().getId());
        if(relationship == null || !relationship.getIsBindMh()){
            return CommonUtil.response(ResponseType.NOT_BIND_MH,"未绑定MH设备",null);
        }
        String hwId = relationship.getMhHwId();
        JSONObject position = mhService.getMHVehiclePosition(hwId);
        if(position != null && "0".equals(position.getString("errno"))){
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",position.getJSONObject("data"));
        }else{
            return CommonUtil.response(ResponseType.REQUEST_FAIL,"请求失败:错误的设备信息或服务器错误",null);
        }
    }

    @RequestMapping(value = "getVehicleStatus", method = RequestMethod.GET)
    public JSONObject getVehicleStatus(){
        UserVehicleRelationship relationship = mhService.getDefaultMHDevice(hostHolder.getAdmin().getId());
        if(relationship == null || !relationship.getIsBindMh()){
            return CommonUtil.response(ResponseType.NOT_BIND_MH,"未绑定MH设备",null);
        }
        String hwId = relationship.getMhHwId();
        JSONObject status = mhService.getMHVehicleStatus(hwId);
        if(status != null && "0".equals(status.getString("errno"))){
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",status.getJSONArray("data"));
        }else{
            return CommonUtil.response(ResponseType.REQUEST_FAIL,"请求失败:错误的设备信息或服务器错误",null);
        }
    }

    @RequestMapping(value = "vehicleSetting", method = RequestMethod.POST)
    public JSONObject vehicleSetting(@RequestParam(value = "key") String key,
                                     @RequestParam(value = "value") String value){
        UserVehicleRelationship relationship = mhService.getDefaultMHDevice(hostHolder.getAdmin().getId());
        if(relationship == null || !relationship.getIsBindMh()){
            return CommonUtil.response(ResponseType.NOT_BIND_MH,"未绑定MH设备",null);
        }
        String hwId = relationship.getMhHwId();
        JSONObject ret = mhService.vehicleSetting(hwId,key,value);
        if(ret != null && "0".equals(ret.getString("errno"))){
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"控制命令下发成功",ret);
        }else{
            return CommonUtil.response(ResponseType.REQUEST_FAIL,"控制命令下发失败",null);
        }
    }


    @PostMapping(value = "/notify",consumes = MediaType.TEXT_PLAIN_VALUE)
    @Authority
    public JSONObject vehicleNotify(@RequestBody String info){
        MHNotifyInfos infos = JSON.parseObject(info,MHNotifyInfos.class);
        mhService.handlerNotify(infos);
        JSONObject ret = new JSONObject();
        ret.put("errno",0);
        ret.put("error","success");
        return ret;
    }

    @GetMapping("/lastestNotify")
    @Authority(AuthorityType.UserValidate)
    public Page<MHNotifyInfos> getMhNotifyInfos(@RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                                                @RequestParam(value = "pageSize",defaultValue = "6")int pageSize){
        return mhService.getMHNotifyInfosList(hostHolder.getAdmin().getId(),pageNum-1,pageSize);
    }

}
