package com.nbicc.cu.carsunion.util;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * Created by bigmao on 2017/8/18.
 */
public class MessageDigestUtil {
    private static String byteArrayToHexString(byte b[]) {
        char buf[] = new char[b.length * 2];
        for (int i = 0; i < buf.length;) {
            byte byte0 = b[i / 2];
            buf[i++] = hexDigits[byte0 >>> 4 & 0xf];
            buf[i++] = hexDigits[byte0 & 0xf];
        }
        return new String(buf);
    }

    private static String getMessageDigestString(String method, String origin, String charsetname) {
        if (CommonUtil.isNullOrEmpty(origin)) {
            return null;
        }

        try {
            MessageDigest md = MessageDigest.getInstance(method);
            if (CommonUtil.isNullOrEmpty(charsetname))
                return byteArrayToHexString(md.digest(origin.getBytes()));
            else
                return byteArrayToHexString(md.digest(origin.getBytes(charsetname)));
        } catch (Exception exception) {
            return null;
        }
    }

    public static String MD5Encode(String origin, String charsetname) {
        return getMessageDigestString("MD5", origin, charsetname).toUpperCase();
    }

    private static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f' };

    public static String generateSignSHA1(SortedMap<String,String> paramMap){
        if(paramMap==null){
            return null;
        }
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String, String>> es = paramMap.entrySet();
        Iterator<Map.Entry<String, String>> it = es.iterator();
        if(it.hasNext()){
            Map.Entry<String, String> entry = it.next();
            String k = entry.getKey();
            Object v = entry.getValue();
            if (v != null && !"sign".equals(k)) {
                sb.append(k + "=" + v);
            }
        }
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String k = entry.getKey();
            Object v = entry.getValue();
            if (v != null && !"sign".equals(k)) {
                sb.append("&" + k + "=" + v);
            }
        }
        return getMessageDigestString("SHA1",sb.toString(),"UTF-8");
    }
}
