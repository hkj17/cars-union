package com.nbicc.cu.carsunion.util;

import com.alibaba.fastjson.JSONObject;

import java.util.Collection;
import java.util.UUID;

/**
 * Created by bigmao on 2017/8/18.
 */
public class CommonUtil {

    public static JSONObject response(int code, Object object){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("result_code", code);
        jsonObject.put("data", object);
        return jsonObject;
    }

    public static boolean isNullOrEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String generateUUID32() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String generateUUID16() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        int first = uuid.substring(0, 11).hashCode();
        int second = uuid.substring(11, 22).hashCode();
        int third = uuid.substring(22).hashCode();
        String hex = String.format("%08x", first) + String.format("%08x", second) + String.format("%08x", third);
        return hexToBase64(hex);
    }

    private static String hexToBase64(String hexString) {
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

    public static void main(String[] args) {
        System.out.println(generateUUID16());
    }
}
