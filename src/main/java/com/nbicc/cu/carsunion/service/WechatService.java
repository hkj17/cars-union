package com.nbicc.cu.carsunion.service;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.MessageDigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import static com.nbicc.cu.carsunion.constant.ParameterKeys.wxAccessToken;
import static com.nbicc.cu.carsunion.constant.ParameterValues.*;
import static com.nbicc.cu.carsunion.constant.ParameterKeys.*;

@Service
public class WechatService {
    @Autowired
    private RestTemplate restTemplateForHttps;

    @Autowired
    private RedisTemplate redisTemplate;

    public JSONObject signJsSdk(String url){
        JSONObject result = new JSONObject();
        long timestamp = System.currentTimeMillis();
        String nonceStr = CommonUtil.generateUUID16();
        String jsApiTicket = getWxJsApiTicket();
        if(jsApiTicket == null){
            return null;
        }
        SortedMap<String,String> sortMap = new TreeMap<>();
        sortMap.put("noncestr",nonceStr);
        sortMap.put("jsapi_ticket",jsApiTicket);
        sortMap.put("timestamp",String.valueOf(timestamp));
        sortMap.put("url",url);
        String sign = MessageDigestUtil.generateSignSHA1(sortMap);
        result.put("appId",WX_APP_ID);
        result.put("timestamp",timestamp);
        result.put("nonceStr",nonceStr);
        result.put("signature",sign);
        return result;
    }

    private String getWxJsApiTicket(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String ticket = (String) valueOperations.get(wxJsApiTicket);
        if(CommonUtil.isNullOrEmpty(ticket)){
            return getWxJsApiTicketFromWx();
        }else {
            return ticket;
        }
    }

    private String getWxJsApiTicketFromWx() {
        String wxAccessToken = getWxAccessToken();
        if(wxAccessToken == null){
            return null;
        }
        JSONObject response = restTemplateForHttps.getForObject(WX_JSAPI_TICKET_URL + "?access_token={1}&type=jsapi",JSONObject.class,wxAccessToken);
        String jsApiTicket = response.getString("ticket");
        if(CommonUtil.isNullOrEmpty(jsApiTicket)){
            return null;
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(wxJsApiTicket,jsApiTicket);
        redisTemplate.expire(wxJsApiTicket,118, TimeUnit.MINUTES);
        return jsApiTicket;
    }

    private String getWxAccessToken(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String accessToken = (String) valueOperations.get(wxAccessToken);
        if(CommonUtil.isNullOrEmpty(accessToken)){
            return getWxAccessTokenFromWx();
        }else{
            return accessToken;
        }
    }

    private String getWxAccessTokenFromWx() {
        String resultStr = restTemplateForHttps.getForObject(WX_ACCESS_TOKEN_URL + "?grant_type=client_credential&appid={1}&secret={2}",String.class,WX_APP_ID,WX_APP_SECRET);
        System.out.println(resultStr);
        String token = ((JSONObject) JSONObject.parse(resultStr)).getString("access_token");
        if(CommonUtil.isNullOrEmpty(token)){
            return null;
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(wxAccessToken,token);
        redisTemplate.expire(wxAccessToken,118, TimeUnit.MINUTES);
        return token;
    }
}
