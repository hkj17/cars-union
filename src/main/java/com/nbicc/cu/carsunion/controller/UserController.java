package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.dao.TokenDao;
import com.nbicc.cu.carsunion.model.Address;
import com.nbicc.cu.carsunion.model.Token;
import com.nbicc.cu.carsunion.model.Vehicle;
import com.nbicc.cu.carsunion.model.VipLevel;
import com.nbicc.cu.carsunion.service.UserService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    TokenDao tokenDao;

    @RequestMapping(value = "/userRegister",  method = RequestMethod.POST)
    public JSONObject modifyUserInfo(HttpServletRequest request,
                                     @RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "nickname", required = false) String nickname,
                                     @RequestParam(value = "portrait", required = false) String portrait,
                                     @RequestParam(value = "contact") String contact,
                                     @RequestParam(value = "password") String password,
                                     @RequestParam(value = "smsCode") String smsCode,
                                     @RequestParam(value = "recommend", required = false) String recommend,
                                     @RequestParam(value = "token") String tokenString){
        if(userService.validateToken(tokenString) == null){
            return CommonUtil.response(ParameterKeys.NOT_AUTHORIZED, "not authorized");
        }
        int state = userService.userRegister(request,name,nickname,contact,portrait,recommend,password,smsCode);
        if(state == 0){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,"ok");
        }else{
            return CommonUtil.response(state, "error");
        }
    }

    @RequestMapping(value = "/modifyUserInfo",  method = RequestMethod.POST)
    public JSONObject modifyUserInfo(HttpServletRequest request,
                                     @RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "nickname", required = false) String nickname,
                                     @RequestParam(value = "portrait", required = false) String portrait,
                                     @RequestParam(value = "contact", required = false) String contact,
                                     @RequestParam(value = "smsCode", required = false) String smsCode,
                                     @RequestParam(value = "token") String tokenString){
        String userId = userService.validateToken(tokenString);
        if(userId == null){
            return CommonUtil.response(ParameterKeys.NOT_AUTHORIZED, "not authorized");
        }
        int state = userService.modifyUserInfo(request,userId,name,nickname,contact,portrait,smsCode);
        if(state == 0){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,"ok");
        }else{
            return CommonUtil.response(state, "error");
        }
    }

    @RequestMapping(value = "/getAddressList",  method = RequestMethod.POST)
    public JSONObject addAddress(@RequestParam(value = "token") String tokenString){
        String userId = userService.validateToken(tokenString);
        if(userId == null){
            return CommonUtil.response(ParameterKeys.NOT_AUTHORIZED, "not authorized");
        }
        List<Address> addressList = userService.getAddressList(userId);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,addressList);
    }

    @RequestMapping(value = "/addAddress",  method = RequestMethod.POST)
    public JSONObject addAddress(@RequestParam(value = "address") String address,
                                  @RequestParam(value = "token") String tokenString,
                                  @RequestParam(value = "default") Boolean isDefault){
        String userId = userService.validateToken(tokenString);
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

    @RequestMapping(value = "/setDefaultAddress",  method = RequestMethod.POST)
    public JSONObject setDefaultAddress(@RequestParam(value = "addressId") String addressId,
                                        @RequestParam(value = "token") String tokenString){
        String userId = userService.validateToken(tokenString);
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

    @RequestMapping(value = "/addVehicle",  method = RequestMethod.POST)
    public JSONObject addVehicle(@RequestParam(value = "vehicleId") String vehicleId,
                                 @RequestParam(value = "token") String tokenString,
                                 @RequestParam(value = "default") Boolean isDefault){
        String userId = userService.validateToken(tokenString);
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

    @RequestMapping(value = "/getVehicles",  method = RequestMethod.POST)
    public JSONObject getVehicles(@RequestParam(value = "token") String tokenString){
        String userId = userService.validateToken(tokenString);
        if(userId == null){
            return CommonUtil.response(ParameterKeys.NOT_AUTHORIZED, "not authorized");
        }
        Set<Vehicle> vehicles = userService.getVehicleList(userId);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,vehicles);
    }

    @RequestMapping(value = "/setDefaultVehicle",  method = RequestMethod.POST)
    public JSONObject setDefaultVehicle(@RequestParam(value = "vehicleId") String vehicleId,
                                         @RequestParam(value = "token") String tokenString){
        String userId = userService.validateToken(tokenString);
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

    @RequestMapping(value = "/getVipLevel",  method = RequestMethod.POST)
    public JSONObject getVipLevel(@RequestParam(value = "token") String tokenString){
        String userId = userService.validateToken(tokenString);
        if(userId == null){
            return CommonUtil.response(ParameterKeys.NOT_AUTHORIZED, "not authorized");
        }
        VipLevel vipLevel = userService.getVipLevelByUser(userId);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,vipLevel);
    }
}
