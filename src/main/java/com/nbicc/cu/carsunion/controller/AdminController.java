package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.model.Admin;
import com.nbicc.cu.carsunion.model.Merchant;
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

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JSONObject login(HttpServletRequest request,
                            @RequestParam(value = "username") String username,
                            @RequestParam(value = "password") String password) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            Admin admin = adminService.getAdminByUserName(username);
            Admin validatedAdmin = adminService.validatePassword(admin, password);
            if (validatedAdmin == null) {
                request.getSession().removeAttribute("user");
                res.put("login_status", 1);
                return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,res);
            }else{
                res.put("login_status", 0);
            }

            validatedAdmin.setUserPasswd(null);
            request.getSession().setAttribute("user", validatedAdmin);
            res.put("admin", validatedAdmin);
            if (validatedAdmin.getAuthority() == 1) {
                Merchant merchant = adminService.getMerchantById(validatedAdmin.getId());
                res.put("merchant", merchant);
            } else if (validatedAdmin.getAuthority() == 2) {

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
            res.put("login_status", 1);
        }
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,res);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }


}
