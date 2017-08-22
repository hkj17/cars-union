package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.http.data.RegionalInfo;
import com.nbicc.cu.carsunion.service.MerchantService;
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
    MerchantService merchantService;

    @RequestMapping(value = "/getRegion", method = RequestMethod.POST)
    public JSONObject getRegion(@RequestParam(value = "province",required = false) String province,
                            @RequestParam(value = "city",required = false) String city,
                            @RequestParam(value = "district",required = false) String district) {
        List<RegionalInfo> regionalInfoList = merchantService.getRegionalInfoList(province,city,district);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS, regionalInfoList);
    }

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
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,"ok");
    }
}
