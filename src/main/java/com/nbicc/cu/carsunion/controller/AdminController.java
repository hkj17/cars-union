package com.nbicc.cu.carsunion.controller;

import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.model.Admin;
import com.nbicc.cu.carsunion.model.Merchant;
import com.nbicc.cu.carsunion.service.AdminService;
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
    public Map<String, Object> login(HttpServletRequest request,
                                     @RequestParam(value = "username") String username,
                                     @RequestParam(value = "password") String password) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            Admin admin = adminService.getAdminByUserName(username);
            adminService.validatePassword(admin, password);
            if (admin == null) {
                res.put(ParameterKeys.STATE, 1);
                return res;
            }

            admin.setUserPasswd("");
            request.getSession().setAttribute("user", admin);
            res.put("admin", admin);
            if (admin.getAuthority() == 1) {
                Merchant merchant = adminService.getMerchantById(admin.getId());
                res.put("merchant", merchant);
            } else if (admin.getAuthority() == 2) {

            } else {

            }
            res.put(ParameterKeys.STATE, 0);
        } catch (Exception e) {
            e.printStackTrace();
            res.put(ParameterKeys.STATE, 1);
        }

        return res;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }


}
