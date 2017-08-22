package com.nbicc.cu.carsunion.http;

import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.HttpRequestUtil;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    protected  String url = "";
    protected String response = "";
    protected Map<String, Object> paramMap;

    public HttpRequest(String url){
        this.url = url;
        paramMap = new HashMap<String, Object>();
    }

    public void setParamater(String key, Object value) {
        paramMap.put(key,value);
    }

    public Object getParameter(String key){
        return paramMap.get(key);
    }

    public String getResponse(){
        if(CommonUtil.isNullOrEmpty(response)){
            response = sendHttpRequest();
        }
        return response;
    }

    private String sendHttpRequest(){
        return HttpRequestUtil.sendGet(url,paramMap);
    }
}
