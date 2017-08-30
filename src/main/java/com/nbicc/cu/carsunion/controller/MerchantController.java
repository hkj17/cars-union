package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.ParameterKeys;

import com.nbicc.cu.carsunion.model.Admin;
import com.nbicc.cu.carsunion.model.Order;
import com.nbicc.cu.carsunion.service.OrderService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/merchant")
public class MerchantController {

    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/getOrderList", method = RequestMethod.POST)
    public JSONObject getOrderListByMerchantId(HttpServletRequest request,
                                               @RequestParam(value = "start", required = false) String startDate,
                                               @RequestParam(value = "end", required = false) String endDate){
        String merchantId = ((Admin) request.getSession().getAttribute("user")).getId();
        List<Order> orders = orderService.getOrderListByMerchantAndTime(merchantId, startDate, endDate);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,orders);
    }
}
