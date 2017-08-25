package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.http.data.Token;
import com.nbicc.cu.carsunion.model.Admin;
import com.nbicc.cu.carsunion.model.Merchant;
import com.nbicc.cu.carsunion.model.User;
import com.nbicc.cu.carsunion.service.AdminService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AdminController {

    @Autowired
    AdminService adminService;

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
        Token token = new Token();
        token.setToken(CommonUtil.generateUUID16());
        token.setExpiresAt(System.currentTimeMillis() + 2 * 3600 * 1000);
        res.put("token", token);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,res);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }

}
