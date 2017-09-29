package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.service.WxpayService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    WxpayService wxpayService;

    @RequestMapping(value = "testPay", method = RequestMethod.POST)
    public JSONObject testPay() {
        Map response = wxpayService.orderQuery();
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS, response);
    }
}
