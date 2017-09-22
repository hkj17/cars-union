package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.constant.ParameterValues;
import com.nbicc.cu.carsunion.http.UnifiedOrderHttpRequest;
import org.springframework.stereotype.Service;

@Service
public class WxpayService {

    UnifiedOrderHttpRequest httpRequest;

    public WxpayService(){
        httpRequest = new UnifiedOrderHttpRequest();
    }

    public String unifiedOrder(){
        httpRequest.setParameter("appid", ParameterValues.APP_ID);
        httpRequest.setParameter("body", "test");
        httpRequest.setParameter("total_fee","1");
        httpRequest.setParameter("mch_id","1485889482");
        httpRequest.setParameter("spbill_create_ip","127.0.0.1");
        httpRequest.setParameter("device_info", "WEB");
        httpRequest.setParameter("nonce_str","TEST");
        httpRequest.setParameter("notify_url","127.0.0.1");
        httpRequest.setParameter("out_trade_no","123456789");
        httpRequest.setParameter("trade_type", "APP");
        return httpRequest.getResponsePOST();
    }
}
