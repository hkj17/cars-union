package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.Authority;
import com.nbicc.cu.carsunion.constant.AuthorityType;
import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.model.HostHolder;
import com.nbicc.cu.carsunion.model.Merchant;
import com.nbicc.cu.carsunion.model.Order;
import com.nbicc.cu.carsunion.service.MerchantService;
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
@Authority
public class MerchantController {

    @Autowired
    OrderService orderService;
    @Autowired
    MerchantService merchantService;
    @Autowired
    HostHolder hostHolder;

    @Authority(value = AuthorityType.MerchantValidate)
    @RequestMapping(value = "/getOrderList", method = RequestMethod.POST)
    public JSONObject getOrderListByMerchantId(@RequestParam(value = "start", required = false) String startDate,
                                               @RequestParam(value = "end", required = false) String endDate){
        String merchantId = hostHolder.getAdmin().getId();
        List<Order> orders = orderService.getOrderListByMerchantAndTime(merchantId, startDate, endDate);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,orders);
    }

    @Authority(value = AuthorityType.UserValidate)
    @RequestMapping(value = "/getMerchantListByRegion", method = RequestMethod.POST)
    public JSONObject getMerchantListByRegion(@RequestParam(value = "region", required =  false) String region){
        List<Merchant> merchantList = merchantService.getMerchantListByRegion(region);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,merchantList);
    }

    @Authority(value = AuthorityType.UserValidate)
    @RequestMapping(value = "/getMerchantByContact", method = RequestMethod.POST)
    public JSONObject getMerchantByContact(@RequestParam(value = "contact") String contact){
        Merchant merchant = merchantService.getMerchantByContact(contact);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,merchant);
    }

    @Authority(value = AuthorityType.UserValidate)
    @RequestMapping(value = "/getMerchantListByKeyword", method = RequestMethod.POST)
    public JSONObject getMerchantListByKeyword(@RequestParam(value = "keyword") String keyword){
        List<Merchant> merchantList = merchantService.getMerchantListByKeyword(keyword);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,merchantList);
    }
}
