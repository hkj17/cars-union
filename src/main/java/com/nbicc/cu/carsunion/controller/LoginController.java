package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.enumtype.ResponseType;
import com.nbicc.cu.carsunion.model.Admin;
import com.nbicc.cu.carsunion.model.Merchant;
import com.nbicc.cu.carsunion.model.ResponseCode;
import com.nbicc.cu.carsunion.model.User;
import com.nbicc.cu.carsunion.service.AdminService;
import com.nbicc.cu.carsunion.service.MerchantService;
import com.nbicc.cu.carsunion.service.UserService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bigmao on 2017/8/18.
 */
@RestController
public class LoginController {

    @Autowired
    AdminService adminService;

    @Autowired
    MerchantService merchantService;

    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate redisTemplate;

    @RequestMapping(value = "/merchantLogin", method = RequestMethod.POST)
    public JSONObject merchantLogin(@RequestParam(value = "username") String username,
                                    @RequestParam(value = "password") String password) {
        Map<String, Object> res = new HashMap<String, Object>();
        Admin admin = adminService.getAdminByUserNameAndAuthority(username, 1);
        Admin validatedAdmin = adminService.validatePassword(admin, password);
        if (validatedAdmin == null) {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "登录失败",null);
        }

        validatedAdmin.setUserPasswd(null);
        String token = adminService.getToken(redisTemplate,admin.getId());
        res.put("token", token);
        res.put("admin", validatedAdmin);
        Merchant merchant = adminService.getMerchantById(validatedAdmin.getId());
        res.put("merchant", merchant);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "登录成功", res);
    }

    @RequestMapping(value = "/adminLogin", method = RequestMethod.POST)
    public JSONObject adminLogin(@RequestParam(value = "username") String username,
                                 @RequestParam(value = "password") String password) {
        Map<String, Object> res = new HashMap<String, Object>();
        Admin admin = adminService.getAdminByUserNameAndAuthority(username, 0);
        Admin validatedAdmin = adminService.validatePassword(admin, password);
        if (validatedAdmin == null) {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "登录失败",null);
        }

        validatedAdmin.setUserPasswd(null);
        String token = adminService.getToken(redisTemplate,admin.getId());
        res.put("token", token);
        res.put("admin", validatedAdmin);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "登录成功", res);
    }

    @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    public JSONObject userLogin(@RequestParam(value = "username") String username,
                                @RequestParam(value = "password") String password) {
        Map<String, Object> res = new HashMap<String, Object>();
        Admin admin = adminService.getAdminByUserNameAndAuthority(username, 2);
        Admin validatedAdmin = adminService.validatePassword(admin, password);
        if (validatedAdmin == null) {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "登录失败",null);
        }

        User user = adminService.getUserById(admin.getId());
        res.put("user", user);
        String token = adminService.getToken(redisTemplate,admin.getId());
        res.put("token", token);
        String updateToken = adminService.getUpdateToken(redisTemplate,admin.getId());
        res.put("updateToken", updateToken);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "登录成功", res);
    }

    @RequestMapping(value = "/updateToken", method = RequestMethod.POST)
    public JSONObject updateToken(@RequestParam(value = "userId") String userId,
                                   @RequestParam(value = "updateToken") String updateToken) {
        String token = adminService.updateToken(redisTemplate,userId,updateToken);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"更新令牌成功", token);
    }

    // todo
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout() {
    }

    @RequestMapping(value = "/merchantRegister", method = RequestMethod.POST)
    public JSONObject merchantRegister(@RequestParam(value = "name") String name,
                                       @RequestParam(value = "address") String address,
                                       @RequestParam(value = "region") String region,
                                       @RequestParam(value = "contact") String contact,
                                       @RequestParam(value = "longitude") String longitude,
                                       @RequestParam(value = "latitude") String latitude,
                                       @RequestParam(value = "idcardFront") String idcardFront,
                                       @RequestParam(value = "idcardBack") String idcardBack,
                                       @RequestParam(value = "license") String license,
                                       @RequestParam(value = "logo") String logo,
                                       @RequestParam(value = "smsCode") String smsCode) {
        ResponseCode state = merchantService.merchantRegister(redisTemplate, name, address, region, contact, longitude, latitude,
                idcardFront, idcardBack, license, logo, smsCode);
        return CommonUtil.response(state,null);
    }

    @RequestMapping(value = "/userRegister", method = RequestMethod.POST)
    public JSONObject modifyUserInfo(@RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "nickname", required = false) String nickname,
                                     @RequestParam(value = "portrait", required = false) String portrait,
                                     @RequestParam(value = "contact") String contact,
                                     @RequestParam(value = "password") String password,
                                     @RequestParam(value = "smsCode") String smsCode,
                                     @RequestParam(value = "recommend", required = false) String recommend) {
        ResponseCode state = userService.userRegister(redisTemplate, name, nickname, contact, portrait, recommend,
                password, smsCode);
        return CommonUtil.response(state, null);
    }

}
