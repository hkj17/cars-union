package com.nbicc.cu.carsunion.http;

import com.alibaba.fastjson.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WxpayHttpResponse {
    private String content;
    private Map<String, String> paramMap;
    private boolean isParamMapSet;

    public WxpayHttpResponse(String content){
        this.content = content;
        this.paramMap = new TreeMap<String, String>();
        this.isParamMapSet = false;
    }

    public String getParamValue(String key) {
        if(isParamMapSet) {
            return paramMap.get(key);
        }else {
            return "";
        }
    }

    public Map getParamMap() {
       return paramMap;
    }

    public void getAllParameters(){
        getAllXmlParams();
    }


    private void getAllXmlParams() {
        int startIndex = content.indexOf("<xml>") + "<xml>".length();
        int endIndex = content.indexOf("</xml>");
        String body = content.substring(startIndex, endIndex).trim();
        String regEx = "<(\\w+)>(.*)</\\w+>";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(body);
        while (m.find()) {
            String key = m.group(1);
            String value = m.group(2);
            String regEx2 = "<!\\[CDATA\\[(.*)\\]\\]>";
            Pattern p2 = Pattern.compile(regEx2);
            Matcher m2 = p2.matcher(value);
            if(m2.find()) {
                paramMap.put(key, m2.group(1));
            }else {
                paramMap.put(key, value);
            }
        }
        isParamMapSet = true;
    }
}
