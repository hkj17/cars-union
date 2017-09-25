package com.nbicc.cu.carsunion.http;

import com.nbicc.cu.carsunion.constant.ParameterValues;

public class CloseOrderHttpRequest extends WxpayHttpRequest {
    public CloseOrderHttpRequest(){
        super(ParameterValues.URL_CLOSE_ORDER);
    }
}
