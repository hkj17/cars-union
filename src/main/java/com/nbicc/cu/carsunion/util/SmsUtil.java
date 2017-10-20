package com.nbicc.cu.carsunion.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class SmsUtil {
    public static boolean verifySmsCode(RedisTemplate redisTemplate, String phone, String smsCode){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String sms = (String) valueOperations.get("verify"+phone);
        if(CommonUtil.isNullOrEmpty(sms) || !sms.equals(smsCode)){
            return false;
        }else{
            return true;
        }
    }
}
