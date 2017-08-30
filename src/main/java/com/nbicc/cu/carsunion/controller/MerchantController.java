package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.ParameterKeys;

import com.nbicc.cu.carsunion.model.Order;
import com.nbicc.cu.carsunion.service.OrderService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/merchant")
public class MerchantController {

    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/getOrderListByMerchantId", method = RequestMethod.POST)
    public JSONObject getOrderListByMerchantId(@RequestParam(value = "merchantId")String merchantId){
        List<Order> orders = orderService.getOrderListByUserId(merchantId);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,orders);
    }
}
