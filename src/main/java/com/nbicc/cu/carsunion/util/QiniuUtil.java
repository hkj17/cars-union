package com.nbicc.cu.carsunion.util;

import com.qiniu.util.Auth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.nbicc.cu.carsunion.constant.ParameterValues.*;

/**
 * Created by bigmao on 2017/8/22.
 */
public class QiniuUtil {

    //输入fileName,输出完整url，公开地址
    public static String photoUrlForPublic(String fileName){
        try {
            String encodedFileName = URLEncoder.encode(fileName, "utf-8");
            String finalUrl = String.format("%s/%s", QINIU_PUBLIC_DOMAIN_OF_BUCKET,encodedFileName);
            return finalUrl;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    //输入fileName,输出完整url，私密地址，授权1小时更新一次
    public static String photoUrlForPrivate(String fileName){
        String domainOfBucket = "http://ov2bdk57a.bkt.clouddn.com";
        try {
            String encodedFileName = URLEncoder.encode(fileName, "utf-8");
            String publicUrl = String.format("%s/%s", domainOfBucket,encodedFileName);
            Auth auth = Auth.create(QINIU_ACCESS_KEY, QINIU_SECRET_KEY);
            long expireInSeconds = 3600;  //two hours
            String finalUrl = auth.privateDownloadUrl(publicUrl,expireInSeconds);
            return finalUrl;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
