package com.nbicc.cu.carsunion.http;

import com.nbicc.cu.carsunion.constant.ParameterValues;

public class OrderQueryHttpRequest extends WxpayHttpRequest{
    public OrderQueryHttpRequest(){
        super(ParameterValues.URL_ORDER_QUERY);
    }
}
