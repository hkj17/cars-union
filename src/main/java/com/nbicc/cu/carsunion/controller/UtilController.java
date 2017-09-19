package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.http.RegionalInfoHttpRequest;
import com.nbicc.cu.carsunion.http.data.RegionalInfo;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.qiniu.util.Auth;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.nbicc.cu.carsunion.constant.ParameterValues.*;

/**
 * Created by bigmao on 2017/8/21.
 */
@RestController
@RequestMapping("/util")
public class UtilController {
    private static final Logger logger = Logger.getLogger(UtilController.class);

    RegionalInfoHttpRequest httpRequest = new RegionalInfoHttpRequest();

    @Autowired
    private RedisTemplate redisTemplate;

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
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,jsonObject);
    }

    //短信
    @RequestMapping(value = "/getSmsCode",method = RequestMethod.POST)
    public JSONObject getSmsCode(@RequestParam(value = "phone",required = false) String phone)
            throws ApiException {
        int num = (int) (Math.random() * 900000 + 100000);
        String message = String.valueOf(num);

        //增加redis保存,10min过期
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("verify"+phone, message);
        redisTemplate.expire("verify"+phone,10, TimeUnit.MINUTES);

        TaobaoClient client = new DefaultTaobaoClient(ALI_DAYU_URL, ALI_DAYU_APPKEY, ALI_DAYU_SECRET);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("123456");
        req.setSmsType("normal");
        req.setSmsFreeSignName("nbicc开发者中心");
        String json = "{\"number\":\""+ message + "\"}";
        req.setSmsParamString(json);
        req.setRecNum(phone);
        req.setSmsTemplateCode("SMS_85130007");
        logger.info("----send message to : " + phone + ", verification code is : " + message);
        try{
            AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
            logger.info("----send result : " + rsp.getBody());
            if(rsp==null){
                return CommonUtil.response(ParameterKeys.REQUEST_FAIL,"error");
            }
            if(rsp.getResult()==null){
                return CommonUtil.response(ParameterKeys.REQUEST_FAIL,"error");
            }
            if (rsp.getResult().getSuccess()) {
                return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,"ok");
            } else {
                return CommonUtil.response(ParameterKeys.REQUEST_FAIL,"error");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/getRegion", method = RequestMethod.POST)
    public JSONObject getRegion(@RequestParam(value = "province",required = false) String province,
                                @RequestParam(value = "city",required = false) String city,
                                @RequestParam(value = "district",required = false) String district) {
        List<RegionalInfo> regionalInfoList = httpRequest.getDistricts(province,city,district);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS, regionalInfoList);
    }

}
