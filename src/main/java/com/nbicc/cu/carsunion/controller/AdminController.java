package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.model.Merchant;
import com.nbicc.cu.carsunion.service.MerchantService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.QiniuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    MerchantService merchantService;

    @RequestMapping(value = "/getRegInProcessList", method = RequestMethod.GET)
    public JSONObject getRegInProcessList(){
        List<Merchant> merchantList = merchantService.getRegInProcessList();
        for(Merchant m: merchantList){
            m.setIdcardFront(QiniuUtil.photoUrlForPrivate(m.getIdcardFront()));
            m.setIdcardBack(QiniuUtil.photoUrlForPrivate(m.getIdcardBack()));
            m.setLicensePath(QiniuUtil.photoUrlForPrivate(m.getLicensePath()));
        }
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

    @RequestMapping(value = "/modifyMerchantInfo", method = RequestMethod.POST)
    public JSONObject modifyMerchantInfo(HttpServletRequest request,
                                         @RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "address", required = false) String address,
                                         @RequestParam(value = "region", required = false) String region,
                                         @RequestParam(value = "contact", required = false) String contact,
                                         @RequestParam(value = "longtitude", required = false) String longtitude,
                                         @RequestParam(value = "latitude", required = false) String latitude){
        //TODO
        return null;
    }
}
