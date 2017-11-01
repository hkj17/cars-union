package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.nbicc.cu.carsunion.constant.Authority;
import com.nbicc.cu.carsunion.constant.ParameterValues;
import com.nbicc.cu.carsunion.enumtype.ResponseType;
import com.nbicc.cu.carsunion.http.RegionalInfoHttpRequest;
import com.nbicc.cu.carsunion.model.Order;
import com.nbicc.cu.carsunion.model.RegionalInfo;
import com.nbicc.cu.carsunion.service.OrderService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.qiniu.util.Auth;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.nbicc.cu.carsunion.constant.ParameterValues.*;

/**
 * Created by bigmao on 2017/8/21.
 */
@RestController
@RequestMapping("/util")
@Authority
public class UtilController {
    private static final Logger logger = Logger.getLogger(UtilController.class);

    RegionalInfoHttpRequest httpRequest = new RegionalInfoHttpRequest();

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    OrderService orderService;

    //给js提供七牛的uptoken，option为1表示私密上传。
    @RequestMapping(value = "getUptoken", method = RequestMethod.GET)
    public JSONObject getUptoken(@RequestParam(value = "option", required = false) String option) {
        String bucket = "photo";
        if (option != null && option.equals("1")) {
            bucket = "private";
        }
        Auth auth = Auth.create(QINIU_ACCESS_KEY, QINIU_SECRET_KEY);
        String upToken = auth.uploadToken(bucket);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uptoken", upToken);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功", jsonObject);
    }

    //提供公共图片访问的前缀域名
    @GetMapping("getPhotoPrefix")
    public JSONObject getPhotoPrefix(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("prefix", ParameterValues.QINIU_PUBLIC_DOMAIN_OF_BUCKET);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功", jsonObject);
    }

    //短信
    @RequestMapping(value = "/getSmsCode", method = RequestMethod.POST)
    public JSONObject getSmsCode(@RequestParam(value = "phone", required = false) String phone)
            throws ApiException {
        int num = (int) (Math.random() * 900000 + 100000);
        String message = String.valueOf(num);

        //增加redis保存,10min过期
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("verify" + phone, message);
        redisTemplate.expire("verify" + phone, 10, TimeUnit.MINUTES);

        TaobaoClient client = new DefaultTaobaoClient(ALI_DAYU_URL, ALI_DAYU_APPKEY, ALI_DAYU_SECRET);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("123456");
        req.setSmsType("normal");
        req.setSmsFreeSignName("nbicc开发者中心");
        String json = "{\"number\":\"" + message + "\"}";
        req.setSmsParamString(json);
        req.setRecNum(phone);
        req.setSmsTemplateCode("SMS_85130007");
        logger.info("----send message to : " + phone + ", verification code is : " + message);
        try {
            AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
            logger.info("----send result : " + rsp.getBody());
            if (rsp == null || rsp.getResult() == null) {
                return CommonUtil.response(ResponseType.REQUEST_FAIL, "操作失败",null);
            }
            if (rsp.getResult().getSuccess()) {
                return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "短信发送成功",null);
            } else {
                return CommonUtil.response(ResponseType.REQUEST_FAIL, "操作失败",null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/getRegion", method = RequestMethod.POST)
    public JSONObject getRegion(@RequestParam(value = "province", required = false) String province,
                                @RequestParam(value = "city", required = false) String city,
                                @RequestParam(value = "district", required = false) String district) {
        List<RegionalInfo> regionalInfoList = httpRequest.getDistricts(province, city, district);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"查询成功",regionalInfoList);
    }

    //alipay sign the orderId
    @RequestMapping(value = "/signForOrder", method = RequestMethod.POST)
    public JSONObject SignForOrder(@RequestParam(value = "orderId") String orderId){
        JSONObject result = new JSONObject();
        Order order = orderService.getOrderByOrderId(orderId);
        if(CommonUtil.isNullOrEmpty(order)){
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "订单不存在",result);
        }

        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(ParameterValues.ALIPAY_GATEWAY_URL,ParameterValues.ALIPAY_APPID, ALIPAY_PRIVATE_KEY,"json","utf-8", ALIPAY_PUBLIC_KEY,"RSA2");

        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();

        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

        //商品详情
        model.setBody("浙江众航信息技术有限公司");
        //商品名称
        model.setSubject("订单号"+orderId);
        //订单号
        model.setOutTradeNo(orderId);

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        model.setTimeoutExpress("30m");
        //金额
        DecimalFormat df=new DecimalFormat("#.00");
        model.setTotalAmount(df.format(order.getRealMoney()));
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(ParameterValues.ALIPAY_NOTIFY_UTL + "/" + orderId);

        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            String orderString = response.getBody();
            result.put("orderString",orderString);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "请求成功",result);
    }

    // receive alipay's notify
    @RequestMapping(value = "/receiveFromAlipay/{orderId}", method = RequestMethod.POST)
    public JSONObject receiveFromAlipay(HttpServletRequest request,
                                    @PathVariable("orderId") String orderId){
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        try {
            boolean flag = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, "utf-8", "RSA2");
            if(flag){
                String state = orderService.finishPay(orderId);
                return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"支付成功",state);
            }else{
                logger.info("-------- OrderId : " + orderId + "  is not payed");
                return CommonUtil.response(ResponseType.REQUEST_FAIL,"支付失败",null);
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return CommonUtil.response(ResponseType.REQUEST_FAIL,"支付失败",null);
        }
    }


}
