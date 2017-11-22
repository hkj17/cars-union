package com.nbicc.cu.carsunion.http;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class MHHttpRequest extends HttpRequest{

    private static CloseableHttpClient hc2 = HttpClients.createDefault();

    // authorization头信息 - 可视为固定字符串，空格不可忽略
    private static final String AUTHORIZATION = "WSSE realm=mhcs, profile=UsernameToken, type=Username";

    protected String gateway_url ="";

    public MHHttpRequest(String url) {
        super(url);
        super.setHeader("X-WSSE", wsseHead());
        super.setHeader("Authorization", AUTHORIZATION);
    }

    /**
     * 获取wsseHead头信息
     * @return String wsseHead头信息
     *
     * @author zzlh
     */
    private static String wsseHead(){
        String wsseHead = "";
        String apiKey = "52bc3509-7ac4-44e2-ae3a-28f176f894a4";
        String userName = "EQKLDQNVD";
        // 随机数Nonce
        Random random = new Random();
        int nonce =(int)Math.floor((random.nextDouble()*10000000.0));
        // 当前时间
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String created = sf.format(new Date());

        String digestStr = nonce + created + apiKey + userName + "api/hwCallBack";
        digestStr = digestStr.trim().replaceAll("\r", "").replaceAll("\n", "");

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(digestStr.trim().getBytes("UTF-8"));
            String encryptDigest = new BASE64Encoder().encode(messageDigest
                    .digest());

            wsseHead = "UsernameToken Username=" + userName
                    + ", PasswordDigest=" + encryptDigest
                    + ", Nonce=" + nonce
                    + ", Created=" + created;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return wsseHead;
    }
}
