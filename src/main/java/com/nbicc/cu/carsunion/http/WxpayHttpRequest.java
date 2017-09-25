package com.nbicc.cu.carsunion.http;

import com.nbicc.cu.carsunion.constant.ParameterValues;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.MessageDigestUtil;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public abstract class WxpayHttpRequest extends HttpRequest {
    public WxpayHttpRequest(String url){
        super(url);
    }

    protected void createSign(){
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String, Object>> es = paramMap.entrySet();
        Iterator<Map.Entry<String, Object>> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            String k = entry.getKey();
            Object v = entry.getValue();
            if (!CommonUtil.isNullOrEmpty(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + ParameterValues.WXPAY_APP_KEY);

        String sign = MessageDigestUtil.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
        this.setParameter("sign",sign);
    }

    protected String generateXmlBody() {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>\r\n");
        Set<Map.Entry<String, Object>> es = paramMap.entrySet();
        Iterator<Map.Entry<String, Object>> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            String k = entry.getKey();
            Object v = entry.getValue();
            sb.append("<" + k + ">" + v + "</" + k + ">" + "\r\n");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    public void setParameter(String key, String value){
        paramMap.put(key, value);
    }

    public String getResponsePOST(){
        createSign();
        String xml = generateXmlBody();
        return super.getResponsePOST(xml);
    }
}
