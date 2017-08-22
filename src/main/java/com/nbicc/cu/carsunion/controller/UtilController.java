package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.http.RegionalInfoHttpRequest;
import com.nbicc.cu.carsunion.http.data.RegionalInfo;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.qiniu.util.Auth;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.nbicc.cu.carsunion.constant.ParameterKeys.accessKey;
import static com.nbicc.cu.carsunion.constant.ParameterKeys.secretKey;

/**
 * Created by bigmao on 2017/8/21.
 */
@RestController
@RequestMapping("/util")
public class UtilController {

    RegionalInfoHttpRequest httpRequest = new RegionalInfoHttpRequest();

    //给js提供七牛的uptoken，option为1表示私密上传。
    @RequestMapping(value = "getUptoken",method = RequestMethod.GET)
    public JSONObject getUptoken(@RequestParam(value = "option",required = false)String option){
        String bucket = "photo";
        if(option!=null && option.equals("1")){
            bucket = "private";
        }
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uptoken",upToken);
        return jsonObject;
    }

    @RequestMapping(value = "/getRegion", method = RequestMethod.POST)
    public JSONObject getRegion(@RequestParam(value = "province",required = false) String province,
                                @RequestParam(value = "city",required = false) String city,
                                @RequestParam(value = "district",required = false) String district) {
        List<RegionalInfo> regionalInfoList = httpRequest.getDistricts(province,city,district);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS, regionalInfoList);
    }
}
