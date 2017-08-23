package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.model.Merchant;

import com.nbicc.cu.carsunion.service.MerchantService;
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
    MerchantService merchantService;

    @RequestMapping(value = "/merchantRegister", method = RequestMethod.POST)
    public JSONObject merchantRegister(@RequestParam(value = "name") String name,
                                       @RequestParam(value = "address") String address,
                                       @RequestParam(value = "region") String region,
                                       @RequestParam(value = "contact") String contact,
                                       @RequestParam(value = "longitude") String longitude,
                                       @RequestParam(value = "latitude") String latitude,
                                       @RequestParam(value = "idcardFront") String idcardFront,
                                       @RequestParam(value = "idcardBack") String idcardBack,
                                       @RequestParam(value = "license") String license) {
        boolean state = merchantService.merchantRegister(name,address,region,contact,longitude,latitude,idcardFront,idcardBack,license);
        if(state){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,"ok");
        }else{
            return CommonUtil.response(ParameterKeys.REQUEST_FAIL,"error");
        }

    }

    @RequestMapping(value = "/getRegInProcessList", method = RequestMethod.GET)
    public JSONObject getRegInProcessList(){
        List<Merchant> merchantList = merchantService.getRegInProcessList();
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS, merchantList);
    }

    @RequestMapping(value = "/passRegistration", method = RequestMethod.POST)
    public JSONObject passRegistration(@RequestParam(value = "contact") String contact){
        boolean state = merchantService.passRegistration(contact);
        if(state){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS, "ok");
        }else{
            return CommonUtil.response(ParameterKeys.REQUEST_FAIL, "error");
        }
    }

    @RequestMapping(value = "/failRegistration", method = RequestMethod.POST)
    public JSONObject failRegistration(@RequestParam(value = "contact") String contact){
        boolean state = merchantService.failRegistration(contact);
        if(state){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS, "ok");
        }else{
            return CommonUtil.response(ParameterKeys.REQUEST_FAIL, "error");
        }
    }
}
