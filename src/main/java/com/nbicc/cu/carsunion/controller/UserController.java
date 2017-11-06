package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.Authority;
import com.nbicc.cu.carsunion.constant.AuthorityType;
import com.nbicc.cu.carsunion.enumtype.ResponseType;
import com.nbicc.cu.carsunion.model.*;
import com.nbicc.cu.carsunion.service.UserService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
@Authority(value = AuthorityType.UserValidate)
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "/modifyUserInfo", method = RequestMethod.POST)
    public JSONObject modifyUserInfo(@RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "nickname", required = false) String nickname,
                                     @RequestParam(value = "portrait", required = false) String portrait,
                                     @RequestParam(value = "contact", required = false) String contact,
                                     @RequestParam(value = "smsCode", required = false) String smsCode) {
        String userId = hostHolder.getAdmin().getId();
        ResponseCode state = userService.modifyUserInfo(redisTemplate,userId,name,nickname,contact,portrait,smsCode);
        return CommonUtil.response(state, null);
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public JSONObject updatePassword(@RequestParam(value = "oldPassword") String oldPassword,
                                     @RequestParam(value = "newPassword") String newPassword,
                                     @RequestParam(value = "smsCode") String smsCode) {
        String userId = hostHolder.getAdmin().getId();
        ResponseCode state = userService.updatePassword(redisTemplate,userId,oldPassword,newPassword,smsCode);
        return CommonUtil.response(state,null);
    }

    @RequestMapping(value = "/getAddressList", method = RequestMethod.POST)
    public JSONObject getAddressList() {
        String userId = hostHolder.getAdmin().getId();
        List<Address> addressList = userService.getAddressList(userId);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "返回成功",addressList);
    }

    @RequestMapping(value = "/addAddress", method = RequestMethod.POST)
    public JSONObject addAddress(@RequestParam(value = "address") String address,
                                 @RequestParam(value = "default") Boolean isDefault) {
        String userId = hostHolder.getAdmin().getId();
        boolean state = userService.addAddress(userId,address,isDefault);
        if(state){
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "操作失败",null);
        }
    }

    @RequestMapping(value = "/deleteAddress", method = RequestMethod.POST)
    public JSONObject deleteAddress(@RequestParam(value = "addressId") String addressId) {
        String userId = hostHolder.getAdmin().getId();
        boolean state = userService.deleteAddress(userId, addressId);
        if (state) {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "操作失败",null);
        }
    }

    @RequestMapping(value = "/modifyAddress", method = RequestMethod.POST)
    public JSONObject modifyAddress(@RequestParam(value = "addressId") String addressId,
                                    @RequestParam(value = "name") String name) {
        String userId = hostHolder.getAdmin().getId();
        boolean state = userService.modifyAddress(userId,addressId,name);
        if (state) {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "操作失败",null);
        }
    }

    @RequestMapping(value = "/setDefaultAddress", method = RequestMethod.POST)
    public JSONObject setDefaultAddress(@RequestParam(value = "addressId") String addressId) {
        String userId = hostHolder.getAdmin().getId();
        boolean state = userService.setDefaultAddress(userId,addressId);
        if(state){
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "操作失败",null);
        }
    }

    @RequestMapping(value = "/addVehicle", method = RequestMethod.POST)
    public JSONObject addVehicle(@RequestParam(value = "vehicleId") String vehicleId,
                                 @RequestParam(value = "default") Boolean isDefault) {
        String userId = hostHolder.getAdmin().getId();
        boolean state = userService.addVehicle(userId,vehicleId,isDefault);
        if(state){
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "操作失败",null);
        }
    }

    @RequestMapping(value = "/deleteVehicle", method = RequestMethod.POST)
    public JSONObject deleteVehicle(@RequestParam(value = "vehicleId") String vehicleId) {
        String userId = hostHolder.getAdmin().getId();
        boolean state = userService.deleteVehicle(userId,vehicleId);
        if(state){
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "操作失败",null);
        }
    }

    @RequestMapping(value = "/getVehicles", method = RequestMethod.POST)
    public JSONObject getVehicles() {
        String userId = hostHolder.getAdmin().getId();
        Set<Vehicle> vehicles = userService.getVehicleList(userId);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "返回成功",vehicles);
    }

    @RequestMapping(value = "/getDefaultVehicle", method = RequestMethod.POST)
    public JSONObject getDefaultVehicle() {
        String userId = hostHolder.getAdmin().getId();
        Vehicle vehicle = userService.getDefaultVehicle(userId);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "返回成功",vehicle);
    }

    @RequestMapping(value = "/setDefaultVehicle", method = RequestMethod.POST)
    public JSONObject setDefaultVehicle(@RequestParam(value = "vehicleId") String vehicleId) {
        String userId = hostHolder.getAdmin().getId();
        boolean state = userService.setDefaultVehicle(userId, vehicleId);
        if (state) {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "操作失败",null);
        }
    }

    @RequestMapping(value = "/getFavoriteList", method = RequestMethod.POST)
    public JSONObject getFavoriteList(){
        String userId = hostHolder.getAdmin().getId();
        List<Favorite> favoriteList = userService.getFavoriteList(userId);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",favoriteList);
    }

    @RequestMapping(value = "/addFavorite", method = RequestMethod.POST)
    public JSONObject addFavorite(@RequestParam(value = "productId") String productId){
        String userId = hostHolder.getAdmin().getId();
        boolean state = userService.addFavorite(userId,productId);
        if (state) {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "操作失败",null);
        }
    }

    @RequestMapping(value = "/deleteFavorite", method = RequestMethod.POST)
    public JSONObject deleteFavorite(@RequestBody JSONObject json){
        String userId = hostHolder.getAdmin().getId();
        userService.deleteFromFavorite(userId, json.getObject("productIdList", List.class));
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",null);
    }

    @RequestMapping(value = "/getBrowseHistory", method = RequestMethod.POST)
    public JSONObject getBrowseHistory(){
        String userId = hostHolder.getAdmin().getId();
        List<Favorite> browseList = userService.getBrowseHistory(userId);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",browseList);
    }

    @RequestMapping(value = "/deleteBrowseHistory", method = RequestMethod.POST)
    public JSONObject deleteBrowseHistory(@RequestBody JSONObject json){
        String userId = hostHolder.getAdmin().getId();
        userService.deleteFromBrowseHistory(userId, json.getObject("productIdList", List.class));
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",null);
    }

    @RequestMapping(value = "/getVipLevel", method = RequestMethod.POST)
    public JSONObject getVipLevel() {
        String userId = hostHolder.getAdmin().getId();
        VipLevel vipLevel = userService.getVipLevelByUser(userId);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",vipLevel);
    }

    @RequestMapping(value = "/getCreditHistory", method = RequestMethod.POST)
    public JSONObject getCreditHistory(@RequestParam(value = "source") int source){
        String userId = hostHolder.getAdmin().getId();
        List<CreditHistory> creditHistoryList = userService.getUserCreditHistory(userId,source);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",creditHistoryList);
    }
}
