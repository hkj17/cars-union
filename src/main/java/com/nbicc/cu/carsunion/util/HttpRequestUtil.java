package com.nbicc.cu.carsunion.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;

public class HttpRequestUtil {

    private static Logger logger = LogManager.getLogger(HttpRequestUtil.class);
    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param paramMap
     *            请求参数。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, Map<String, Object> paramMap, Map<String,String> propertyMap) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            Iterator<Map.Entry<String,Object>> entryIterator = paramMap.entrySet().iterator();
            if(entryIterator.hasNext()){
                Map.Entry<String,Object> firstEntry = entryIterator.next();
                urlNameString += "?" + firstEntry.getKey() + "=" + firstEntry.getValue();
            }
            while (entryIterator.hasNext()){
                Map.Entry<String, Object> nextEntry = entryIterator.next();
                urlNameString += "&" + nextEntry.getKey() + "=" + nextEntry.getValue();
            }
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            Iterator<Map.Entry<String,String>> propertyIterator = propertyMap.entrySet().iterator();
            while(propertyIterator.hasNext()){
                Map.Entry<String,String> entry = propertyIterator.next();
                connection.setRequestProperty(entry.getKey(),entry.getValue());
            }
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
//            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }
        } catch (Exception e) {
            logger.error("发送GET请求出现异常！" + e.getMessage());
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
//        logger.info("--sendGet result: " + result);
        return result;
    }

    public static String sendPost(String url, String postdata,Map<String,Object> paramMap, Map<String,String> propertyMap) {
        String result = "";
        BufferedReader in = null;
        try {
            byte[] postdataBytes = postdata.getBytes("UTF-8");
            String urlNameString = url;
            Iterator<Map.Entry<String,Object>> entryIterator = paramMap.entrySet().iterator();
            if(entryIterator.hasNext()){
                Map.Entry<String,Object> firstEntry = entryIterator.next();
                urlNameString += "?" + firstEntry.getKey() + "=" + firstEntry.getValue();
            }
            while (entryIterator.hasNext()){
                Map.Entry<String,Object> entry = entryIterator.next();
                urlNameString += "&" + entry.getKey() + "=" + entry.getValue();
            }
            System.out.println("urlNameString : " + urlNameString);
            URL realUrl = new URL(urlNameString);
            HttpURLConnection httpConn=(HttpURLConnection)realUrl.openConnection();
            httpConn.setDoOutput(true);   //需要输出
            httpConn.setDoInput(true);   //需要输入
            httpConn.setUseCaches(false);  //不允许缓存
            httpConn.setRequestMethod("POST");   //设置POST方式连接
            //设置请求属性
            httpConn.setRequestProperty("Content-Type", "application/Json; charset=UTF-8");
            httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            httpConn.setRequestProperty("Charset", "UTF-8");

            Iterator<Map.Entry<String,String>> propertyIterator = propertyMap.entrySet().iterator();
            while(propertyIterator.hasNext()){
                Map.Entry<String,String> entry = propertyIterator.next();
                httpConn.setRequestProperty(entry.getKey(),entry.getValue());
            }
            //连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
            httpConn.connect();
            //建立输入流，向指向的URL传入参数
            DataOutputStream dos=new DataOutputStream(httpConn.getOutputStream());
            dos.write(postdataBytes);
            dos.flush();
            dos.close();

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }
//            logger.info("--sendPost result: " + result);
            return result;
        } catch (Exception e) {
            logger.error("发送POST请求出现异常！" + e.getMessage());
            return null;
        }

    }

//    public static void main(String[] args){
//        String postData = "<xml>\n" +
//                "   <appid>wx2421b1c4370ec43b</appid>\n" +
//                "   <attach>支付测试</attach>\n" +
//                "   <body>APP支付测试</body>\n" +
//                "   <mch_id>10000100</mch_id>\n" +
//                "   <nonce_str>1add1a30ac87aa2db72f57a2375d8fec</nonce_str>\n" +
//                "   <notify_url>http://wxpay.wxutil.com/pub_v2/pay/notify.v2.php</notify_url>\n" +
//                "   <out_trade_no>1415659990</out_trade_no>\n" +
//                "   <spbill_create_ip>14.23.150.211</spbill_create_ip>\n" +
//                "   <total_fee>1</total_fee>\n" +
//                "   <trade_type>APP</trade_type>\n" +
//                "   <sign>0CB01533B8C1EF103065174F50BCA001</sign>\n" +
//                "</xml>";
//        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
//        System.out.println(HttpRequestUtil.sendPost(url,postData));
//    }
}