package com.nbicc.cu.carsunion.http;

import com.nbicc.cu.carsunion.constant.ParameterValues;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.MessageDigestUtil;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class UnifiedOrderHttpRequest extends WxpayHttpRequest {
    public UnifiedOrderHttpRequest(){
        super(ParameterValues.URL_UNIFIED_ORDER);
    }
}
