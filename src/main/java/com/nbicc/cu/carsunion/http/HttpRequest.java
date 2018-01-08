package com.nbicc.cu.carsunion.http;

import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.HttpRequestUtil;

import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.nbicc.cu.carsunion.util.HttpRequestUtil.sendGet;

public class HttpRequest {
    protected  String url = "";
    protected String response = "";
    protected SortedMap<String, Object> paramMap;
    protected HashMap<String,String> propertyMap;

    public HttpRequest(String url){
        this.url = url;
        paramMap = new TreeMap<String, Object>();
        propertyMap = new HashMap<String, String>();
    }

    public String getCachedResponseGET(){
        if(CommonUtil.isNullOrEmpty(response)){
            response = sendHttpRequestGet();
        }
        return response;
    }

    public String getResponseGET(){
        return sendHttpRequestGet();
    }

    public void setParameter(String key, String value){
        paramMap.put(key, value);
    }

    public void setHeader(String key, String value){
        propertyMap.put(key,value);
    }

    public String getResponsePOST(String postdata){
        return sendHttpRequestPost(postdata);
    }

    public String getCachedResponsePost(String postdata){
        if(CommonUtil.isNullOrEmpty(response)){
            return sendHttpRequestPost(postdata);
        }
        return response;
    }

    private String sendHttpRequestGet(){
        return sendGet(url,paramMap,propertyMap);
    }

    private String sendHttpRequestPost(String postdata){
        return HttpRequestUtil.sendPost(url,postdata,paramMap,propertyMap);
    }
}
