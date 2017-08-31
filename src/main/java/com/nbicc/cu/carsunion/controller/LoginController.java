package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.model.Token;
import com.nbicc.cu.carsunion.model.Admin;
import com.nbicc.cu.carsunion.model.Merchant;
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

import javax.servlet.http.HttpServletRequest;
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
    public JSONObject merchantLogin(HttpServletRequest request,
                            @RequestParam(value = "username") String username,
                            @RequestParam(value = "password") String password) {
        Map<String, Object> res = new HashMap<String, Object>();
        Admin admin = adminService.getAdminByUserNameAndAuthority(username,1);
        Admin validatedAdmin = adminService.validatePassword(admin, password);
        if (validatedAdmin == null) {
            request.getSession().removeAttribute("user");
            return CommonUtil.response(ParameterKeys.REQUEST_FAIL,"error");
        }

        validatedAdmin.setUserPasswd(null);
        request.getSession().setAttribute("user", validatedAdmin);
        res.put("admin", validatedAdmin);
        Merchant merchant = adminService.getMerchantById(validatedAdmin.getId());
        res.put("merchant", merchant);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,res);
    }

    @RequestMapping(value = "/adminLogin", method = RequestMethod.POST)
    public JSONObject adminLogin(HttpServletRequest request,
                                 @RequestParam(value = "username") String username,
                                 @RequestParam(value = "password") String password) {
        Map<String, Object> res = new HashMap<String, Object>();
        Admin admin = adminService.getAdminByUserNameAndAuthority(username,0);
        Admin validatedAdmin = adminService.validatePassword(admin, password);
        if (validatedAdmin == null) {
            request.getSession().removeAttribute("user");
            return CommonUtil.response(ParameterKeys.REQUEST_FAIL,"error");
        }

        validatedAdmin.setUserPasswd(null);
        request.getSession().setAttribute("user", validatedAdmin);
        res.put("admin", validatedAdmin);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,res);
    }

    @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    public JSONObject userLogin(HttpServletRequest request,
                                 @RequestParam(value = "username") String username,
                                 @RequestParam(value = "password") String password) {
        Map<String, Object> res = new HashMap<String, Object>();
        Admin admin = adminService.getAdminByUserNameAndAuthority(username,2);
        Admin validatedAdmin = adminService.validatePassword(admin, password);
        if (validatedAdmin == null) {
            return CommonUtil.response(ParameterKeys.REQUEST_FAIL,"error");
        }

        User user = adminService.getUserById(admin.getId());
        res.put("user", user);
        String token = adminService.updateToken(redisTemplate,admin.getId());
        res.put("token", token);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,res);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();
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
                                       @RequestParam(value = "smsCode") String smsCode) {
        int state = merchantService.merchantRegister(redisTemplate,name,address,region,contact,longitude,latitude,idcardFront,idcardBack,license, smsCode);
        if(state == 0){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,"ok");
        }else{
            return CommonUtil.response(state,"error");
        }
    }

    @RequestMapping(value = "/userRegister",  method = RequestMethod.POST)
    public JSONObject modifyUserInfo(@RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "nickname", required = false) String nickname,
                                     @RequestParam(value = "portrait", required = false) String portrait,
                                     @RequestParam(value = "contact") String contact,
                                     @RequestParam(value = "password") String password,
                                     @RequestParam(value = "smsCode") String smsCode,
                                     @RequestParam(value = "recommend", required = false) String recommend){
        int state = userService.userRegister(redisTemplate,name,nickname,contact,portrait,recommend,password,smsCode);
        if(state == 0){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,"ok");
        }else{
            return CommonUtil.response(state, "error");
        }
    }

}
