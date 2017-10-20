package com.nbicc.cu.carsunion.util;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.enumtype.ResponseType;
import com.nbicc.cu.carsunion.model.ResponseCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

/**
 * Created by bigmao on 2017/8/18.
 */
public class CommonUtil {

    public static JSONObject response(ResponseCode code, Object object){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("result_code", code.getResponseType().ordinal());
        jsonObject.put("message",code.getMessage());
        jsonObject.put("data", object);
        return jsonObject;
    }

    public static JSONObject response(ResponseType type, String message, Object object){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("result_code", type.ordinal());
        jsonObject.put("message",message);
        jsonObject.put("data", object);
        return jsonObject;
    }

    public static boolean isNullOrEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNullOrEmpty(Object str) {
        return str == null;
    }

    public static String generateUUID32() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String generateUUID16() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        int first = uuid.substring(0, 16).hashCode();
        int second = uuid.substring(16).hashCode();
        return String.format("%08x", first) + String.format("%08x", second);
    }

    public static String hexToBase64(String hexString) {
        String ret = "";
        for (int i = 0; i < hexString.length(); i += 3) {
            int curr = Integer.parseInt(hexString.substring(i, i + 3), 16) & 0xfff;
            ret = ret + base64Digits[curr / 64];
            ret = ret + base64Digits[curr % 64];
        }
        return ret;
    }

    private final static char[] base64Digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', '+', '/'};

    public static Date String2Date(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.parse(date);
    }
}
