package com.nbicc.cu.carsunion.util;

import com.nbicc.cu.carsunion.constant.ParameterKeys;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.servlet.http.HttpServletRequest;

public class SmsUtil {
    public static boolean verifySmsCode(RedisTemplate redisTemplate, String phone, String smsCode){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //String sms = (String) request.getSession().getAttribute("verify"+phone);
        String sms = (String) valueOperations.get("verify"+phone);
        if(CommonUtil.isNullOrEmpty(sms) || !sms.equals(smsCode)){
            return false;
        }else{
            return true;
        }
    }
}
