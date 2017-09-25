package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.constant.ParameterValues;
import com.nbicc.cu.carsunion.http.CloseOrderHttpRequest;
import com.nbicc.cu.carsunion.http.OrderQueryHttpRequest;
import com.nbicc.cu.carsunion.http.UnifiedOrderHttpRequest;
import com.nbicc.cu.carsunion.http.WxpayHttpResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WxpayService {

    UnifiedOrderHttpRequest unifiedOrderHttpRequest;

    CloseOrderHttpRequest closeOrderHttpRequest;

    OrderQueryHttpRequest orderQueryHttpRequest;

    WxpayHttpResponse wxpayHttpResponse;

    public WxpayService(){
        unifiedOrderHttpRequest = new UnifiedOrderHttpRequest();
        closeOrderHttpRequest = new CloseOrderHttpRequest();
        orderQueryHttpRequest = new OrderQueryHttpRequest();
    }

    public Map unifiedOrder(){
        unifiedOrderHttpRequest.setParameter("appid", ParameterValues.APP_ID);
        unifiedOrderHttpRequest.setParameter("body", "test");
        unifiedOrderHttpRequest.setParameter("total_fee","1");
        unifiedOrderHttpRequest.setParameter("mch_id","1485889482");
        unifiedOrderHttpRequest.setParameter("spbill_create_ip","127.0.0.1");
        unifiedOrderHttpRequest.setParameter("device_info", "WEB");
        unifiedOrderHttpRequest.setParameter("nonce_str","TEST");
        unifiedOrderHttpRequest.setParameter("notify_url","127.0.0.1");
        unifiedOrderHttpRequest.setParameter("out_trade_no","123456789");
        unifiedOrderHttpRequest.setParameter("trade_type", "APP");
        String response = unifiedOrderHttpRequest.getResponsePOST();;
        wxpayHttpResponse = new WxpayHttpResponse(response);
        wxpayHttpResponse.getAllParameters();
        return wxpayHttpResponse.getParamMap();
    }

    public Map closeOrder(){
        closeOrderHttpRequest.setParameter("appid", ParameterValues.APP_ID);
        closeOrderHttpRequest.setParameter("mch_id","1485889482");
        closeOrderHttpRequest.setParameter("out_trade_no","123456789");
        closeOrderHttpRequest.setParameter("nonce_str","TEST");
        String response = closeOrderHttpRequest.getResponsePOST();;
        wxpayHttpResponse = new WxpayHttpResponse(response);
        wxpayHttpResponse.getAllParameters();
        return wxpayHttpResponse.getParamMap();
    }

    public Map orderQuery(){
        orderQueryHttpRequest.setParameter("appid", ParameterValues.APP_ID);
        orderQueryHttpRequest.setParameter("mch_id","1485889482");
        orderQueryHttpRequest.setParameter("out_trade_no","123456789");
        orderQueryHttpRequest.setParameter("nonce_str","TEST");
        String response = orderQueryHttpRequest.getResponsePOST();;
        wxpayHttpResponse = new WxpayHttpResponse(response);
        wxpayHttpResponse.getAllParameters();
        return wxpayHttpResponse.getParamMap();
    }
}
