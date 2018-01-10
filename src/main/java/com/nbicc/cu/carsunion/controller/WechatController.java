package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.Authority;
import com.nbicc.cu.carsunion.enumtype.ResponseType;
import com.nbicc.cu.carsunion.service.WechatService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wechat")
@Authority
public class WechatController {

    @Autowired
    private WechatService wechatService;

    @PostMapping(value = "/sign/sdk")
    public JSONObject signJsSdk(@RequestParam("url")String url){
        JSONObject ret = wechatService.signJsSdk(url);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",ret);
    }
}
