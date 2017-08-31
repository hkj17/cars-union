package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.model.Order;
import com.nbicc.cu.carsunion.model.OrderDetail;
import com.nbicc.cu.carsunion.service.OrderService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by bigmao on 2017/8/28.
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "addOrder", method = RequestMethod.POST)
    public JSONObject addOrder(@RequestBody JSONObject json){
        String merchantId = json.getString("merchantId");
        List<Map> productList = json.getObject("product",List.class);
        String addressId = json.getString("addressId");
        // todo
        String userId = json.getString("token");
        String result = orderService.addOrder(userId,merchantId,addressId,productList);
        if("ok".equals(result)){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,"ok");
        }else{
            return CommonUtil.response(ParameterKeys.REQUEST_FAIL,"wrong");
        }
    }

    // todo 可能要做分页,按订单状态查询
    @RequestMapping(value = "getOrderList", method = RequestMethod.POST)
    public JSONObject getOrderListByUserId(@RequestParam(value = "userId")String userId,
                                           @RequestParam(value = "start")String startDate,
                                           @RequestParam(value = "end")String endDate){
        List<Order> orders = orderService.getOrderListByUserAndTime(userId,startDate,endDate);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,orders);
    }

    // todo 分页
    @RequestMapping(value = "deleteOrder",method = RequestMethod.POST)
    public JSONObject deleteOrder(@RequestParam(value = "id") String id){
        orderService.delete(id);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,"ok");
    }

    @RequestMapping(value = "getOrderByOrderId", method = RequestMethod.POST)
    public JSONObject getOrderByOrderId(@RequestParam(value = "orderId") String orderId){
        Order order = orderService.getOrderByOrderId(orderId);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,order);
    }

    @RequestMapping(value = "getOrderDetailByOrderId", method = RequestMethod.POST)
    public JSONObject getOrderDetailByOrderId(@RequestParam(value = "orderId") String orderId){
        List<OrderDetail> details = orderService.getOrderDetailByOrderId(orderId);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,details);
    }

    //完成支付,改变订单状态为已支付
    @RequestMapping(value = "finishPay",method = RequestMethod.POST)
    public JSONObject finishPay(@RequestParam("orderId")String orderId){
        String result = orderService.finishPay(orderId);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,result);
    }

    //发货，填写物流号
    @RequestMapping(value = "deliverProducts",method = RequestMethod.POST)
    public JSONObject finishPay(@RequestParam("orderId")String orderId,
                                @RequestParam("courierNumber")String courierNumber){
        String result = orderService.deliverProducts(orderId,courierNumber);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,result);
    }


}
