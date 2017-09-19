package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.Authority;
import com.nbicc.cu.carsunion.constant.AuthorityType;
import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.model.Address;
import com.nbicc.cu.carsunion.model.HostHolder;
import com.nbicc.cu.carsunion.model.Vehicle;
import com.nbicc.cu.carsunion.model.VipLevel;
import com.nbicc.cu.carsunion.service.UserService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
@Authority
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    HostHolder hostHolder;

    @Authority(value = AuthorityType.UserValidate)
    @RequestMapping(value = "/modifyUserInfo",  method = RequestMethod.POST)
    public JSONObject modifyUserInfo(@RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "nickname", required = false) String nickname,
                                     @RequestParam(value = "portrait", required = false) String portrait,
                                     @RequestParam(value = "contact", required = false) String contact,
                                     @RequestParam(value = "smsCode", required = false) String smsCode){
        String userId = hostHolder.getAdmin().getId();
        if(userId == null){
            return CommonUtil.response(ParameterKeys.NOT_AUTHORIZED, "not authorized");
        }
        int state = userService.modifyUserInfo(redisTemplate,userId,name,nickname,contact,portrait,smsCode);
        if(state == 0){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,"ok");
        }else{
            return CommonUtil.response(state, "error");
        }
    }

    @Authority(value = AuthorityType.UserValidate)
    @RequestMapping(value = "/updatePassword",  method = RequestMethod.POST)
    public JSONObject updatePassword(@RequestParam(value = "oldPassword") String oldPassword,
                                     @RequestParam(value = "newPassword") String newPassword,
                                     @RequestParam(value = "smsCode") String smsCode){
        String userId = hostHolder.getAdmin().getId();
        if(userId == null){
            return CommonUtil.response(ParameterKeys.NOT_AUTHORIZED, "not authorized");
        }
        int state = userService.updatePassword(redisTemplate,userId,oldPassword,newPassword,smsCode);
        if(state == 0){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,"ok");
        }else{
            return CommonUtil.response(state,"error");
        }
    }

    @Authority(value = AuthorityType.UserValidate)
    @RequestMapping(value = "/getAddressList",  method = RequestMethod.POST)
    public JSONObject getAddressList(){
        String userId = hostHolder.getAdmin().getId();
        if(userId == null){
            return CommonUtil.response(ParameterKeys.NOT_AUTHORIZED, "not authorized");
        }
        List<Address> addressList = userService.getAddressList(userId);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,addressList);
    }

    @Authority(value = AuthorityType.UserValidate)
    @RequestMapping(value = "/addAddress",  method = RequestMethod.POST)
    public JSONObject addAddress(@RequestParam(value = "address") String address,
                                  @RequestParam(value = "default") Boolean isDefault){
        String userId = hostHolder.getAdmin().getId();
        if(userId == null){
            return CommonUtil.response(ParameterKeys.NOT_AUTHORIZED, "not authorized");
        }
        boolean state = userService.addAddress(userId,address,isDefault);
        if(state){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS, "ok");
        }else{
            return CommonUtil.response(ParameterKeys.REQUEST_FAIL, "error");
        }
    }

    @Authority(value = AuthorityType.UserValidate)
    @RequestMapping(value = "/setDefaultAddress",  method = RequestMethod.POST)
    public JSONObject setDefaultAddress(@RequestParam(value = "addressId") String addressId){
        String userId = hostHolder.getAdmin().getId();
        if(userId == null){
            return CommonUtil.response(ParameterKeys.NOT_AUTHORIZED, "not authorized");
        }
        boolean state = userService.setDefaultAddress(userId,addressId);
        if(state){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS, "ok");
        }else{
            return CommonUtil.response(ParameterKeys.REQUEST_FAIL, "error");
        }
    }

    @Authority(value = AuthorityType.UserValidate)
    @RequestMapping(value = "/addVehicle",  method = RequestMethod.POST)
    public JSONObject addVehicle(@RequestParam(value = "vehicleId") String vehicleId,
                                 @RequestParam(value = "default") Boolean isDefault){
        String userId = hostHolder.getAdmin().getId();
        if(userId == null){
            return CommonUtil.response(ParameterKeys.NOT_AUTHORIZED, "not authorized");
        }
        boolean state = userService.addVehicle(userId,vehicleId,isDefault);
        if(state){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS, "ok");
        }else {
            return CommonUtil.response(ParameterKeys.REQUEST_FAIL, "error");
        }
    }

    @Authority(value = AuthorityType.UserValidate)
    @RequestMapping(value = "/getVehicles",  method = RequestMethod.POST)
    public JSONObject getVehicles(){
        String userId = hostHolder.getAdmin().getId();
        if(userId == null){
            return CommonUtil.response(ParameterKeys.NOT_AUTHORIZED, "not authorized");
        }
        Set<Vehicle> vehicles = userService.getVehicleList(userId);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,vehicles);
    }

    @Authority(value = AuthorityType.UserValidate)
    @RequestMapping(value = "/setDefaultVehicle",  method = RequestMethod.POST)
    public JSONObject setDefaultVehicle(@RequestParam(value = "vehicleId") String vehicleId){
        String userId = hostHolder.getAdmin().getId();
        if(userId == null){
            return CommonUtil.response(ParameterKeys.NOT_AUTHORIZED, "not authorized");
        }
        boolean state = userService.setDefaultVehicle(userId, vehicleId);
        if(state){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS, "ok");
        }else {
            return CommonUtil.response(ParameterKeys.REQUEST_FAIL, "error");
        }
    }

    @Authority(value = AuthorityType.UserValidate)
    @RequestMapping(value = "/getVipLevel",  method = RequestMethod.POST)
    public JSONObject getVipLevel(){
        String userId = hostHolder.getAdmin().getId();
        if(userId == null){
            return CommonUtil.response(ParameterKeys.NOT_AUTHORIZED, "not authorized");
        }
        VipLevel vipLevel = userService.getVipLevelByUser(userId);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,vipLevel);
    }
}
