package com.nbicc.cu.carsunion.util;

import com.nbicc.cu.carsunion.constant.ParameterKeys;

import javax.servlet.http.HttpServletRequest;

public class SmsUtil {
    public static boolean verifySmsCode(HttpServletRequest request, String phone, String smsCode){
        String sms = (String) request.getSession().getAttribute("verify"+phone);
        if(CommonUtil.isNullOrEmpty(sms) || !sms.equals(smsCode)){
            return false;
        }else{
            return true;
        }
    }
}
