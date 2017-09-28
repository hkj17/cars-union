package com.nbicc.cu.carsunion.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;

import java.util.Map;

public class SignUtil {
    public String rsaSign(Map<String, String> paramMap, String privateKey) throws AlipayApiException{
        return AlipaySignature.rsaSign(paramMap,privateKey,"UTF-8");
    }

    public boolean rsaCheck(String content, String sign, String publicKey) throws AlipayApiException{
        return AlipaySignature.rsaCheck(content,sign,publicKey,"UTF-8","RSA2");
    }
}
