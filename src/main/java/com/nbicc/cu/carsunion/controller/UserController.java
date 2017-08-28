package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.dao.TokenDao;
import com.nbicc.cu.carsunion.model.Token;
import com.nbicc.cu.carsunion.service.UserService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    TokenDao tokenDao;

    @RequestMapping(value = "/addAddress",  method = RequestMethod.POST)
    public JSONObject addAddress(@RequestParam(value = "address") String address,
                                  @RequestParam(value = "token") String tokenString){
        Token token = tokenDao.findByToken(tokenString);
        if(CommonUtil.isNullOrEmpty(token)){
            return CommonUtil.response(ParameterKeys.NOT_AUTHORIZED, "not authorized");
        }
        boolean state = userService.addAddress(token.getId(),address);
        if(state){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS, "ok");
        }else{
            return CommonUtil.response(ParameterKeys.REQUEST_FAIL, "error");
        }
    }

}
