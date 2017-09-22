package com.nbicc.cu.carsunion.http;

import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.HttpRequestUtil;

import java.util.SortedMap;
import java.util.TreeMap;

public class HttpRequest {
    protected  String url = "";
    protected String response = "";
    protected SortedMap<String, Object> paramMap;

    public HttpRequest(String url){
        this.url = url;
        paramMap = new TreeMap<String, Object>();
    }

    public String getCachedResponseGET(){
        if(CommonUtil.isNullOrEmpty(response)){
            response = sendHttpRequestGet();
        }
        return response;
    }

    public String getResponsePOST(String postdata){
        response = sendHttpRequestPost(postdata);
        return response;
    }

    private String sendHttpRequestGet(){
        return HttpRequestUtil.sendGet(url,paramMap);
    }

    private String sendHttpRequestPost(String postdata){
        return HttpRequestUtil.sendPost(url,postdata);
    }
}
