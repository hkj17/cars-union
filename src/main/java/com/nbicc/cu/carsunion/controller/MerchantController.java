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
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
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

    // todo 可能要做分页,按订单状态查询
    @Authority(value = AuthorityType.MerchantValidate)
    @RequestMapping(value = "/getOrderList", method = RequestMethod.POST)
    public JSONObject getOrderListByMerchantId(@RequestParam(value = "start", defaultValue = "2017-01-01 00:00:00") String startDate,
                                               @RequestParam(value = "end", defaultValue = "2050-01-01 00:00:00") String endDate,
                                               @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        String merchantId = hostHolder.getAdmin().getId();
        Page<Order> orders = null;
        try {
            orders = orderService.getOrderListByMerchantAndTimeWithPage(merchantId, startDate, endDate, pageNum - 1, pageSize);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS, orders);
    }

    @Authority(value = AuthorityType.UserValidate)
    @RequestMapping(value = "/getMerchantByContact", method = RequestMethod.POST)
    public JSONObject getMerchantByContact(@RequestParam(value = "contact") String contact) {
        Merchant merchant = merchantService.getMerchantByContact(contact);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS, merchant);
    }

    @Authority(value = AuthorityType.UserValidate)
    @RequestMapping(value = "/searchMerchant", method = RequestMethod.POST)
    public JSONObject searchMerchant(@RequestParam(value = "region", required = false) String region,
                                     @RequestParam(value = "keyword", required = false) String keyword) {
        List<Merchant> merchantList = merchantService.getMerchantList(region, keyword);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS, merchantList);
    }
}
