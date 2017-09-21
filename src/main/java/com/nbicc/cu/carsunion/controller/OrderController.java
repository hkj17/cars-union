package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.Authority;
import com.nbicc.cu.carsunion.constant.AuthorityType;
import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.model.HostHolder;
import com.nbicc.cu.carsunion.model.Order;
import com.nbicc.cu.carsunion.model.OrderDetail;
import com.nbicc.cu.carsunion.model.ShoppingCart;
import com.nbicc.cu.carsunion.service.OrderService;
import com.nbicc.cu.carsunion.service.UserService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by bigmao on 2017/8/28.
 */
@RestController
@RequestMapping("/order")
@Authority(value = AuthorityType.UserValidate)
public class OrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(value = "addToShoppingCart", method = RequestMethod.POST)
    public JSONObject addToShoppingCart(@RequestParam(value = "productId") String productId,
                                         @RequestParam(value = "quantity") int quantity){
        String userId = hostHolder.getAdmin().getId();
        boolean state = orderService.addProductToShoppingCart(userId,productId, quantity);
        if(state){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,"ok");
        }else{
            return CommonUtil.response(ParameterKeys.REQUEST_FAIL,"error");
        }
    }

    @RequestMapping(value = "deleteFromShoppingCart", method = RequestMethod.POST)
    public JSONObject deleteFromShoppingCart(@RequestBody JSONObject json){
        String userId = hostHolder.getAdmin().getId();
        orderService.deleteFromShoppingCart(userId, json.getObject("productIdList",List.class));
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,"ok");
    }

    @RequestMapping(value = "modifyShoppingCart", method = RequestMethod.POST)
    public JSONObject modifyShoppingCart(@RequestBody JSONObject json){
        String userId = hostHolder.getAdmin().getId();
        orderService.modifyShoppingCart(userId, json.getObject("productIdMap", Map.class));
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,"ok");
    }

    @RequestMapping(value = "getShoppingCartList", method = RequestMethod.POST)
    public JSONObject getShoppingCartList(){
        String userId = hostHolder.getAdmin().getId();
        List<ShoppingCart> shoppingCartList = orderService.getShoppingCartList(userId);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,shoppingCartList);
    }

    @RequestMapping(value = "addOrder", method = RequestMethod.POST)
    public JSONObject addOrder(@RequestBody JSONObject json){
        String merchantId = json.getString("merchantId");
        List<Map> productList = json.getObject("product",List.class);
        String addressId = json.getString("addressId");

        Order newOrder = orderService.addOrder(hostHolder.getAdmin().getId(),merchantId,addressId,productList);
        if(newOrder != null){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,newOrder);
        }else{
            return CommonUtil.response(ParameterKeys.REQUEST_FAIL,"wrong");
        }
    }

    // todo 可能要做分页,按订单状态查询
//    @RequestMapping(value = "getOrderList", method = RequestMethod.POST)
//    public JSONObject getOrderListByUserId(@RequestParam(value = "start", required = false)String startDate,
//                                           @RequestParam(value = "end", required = false)String endDate,
//                                           @RequestParam(value = "pageNum", defaultValue = "1")int pageNum,
//                                           @RequestParam(value = "pageSize", defaultValue = "10")int pageSize){
//        List<Order> orders = orderService.getOrderListByUserAndTime(hostHolder.getAdmin().getId(),startDate,endDate, pageNum, pageSize);
//        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,orders);
//    }

    //带分页的订单列表
    @RequestMapping(value = "getOrderList", method = RequestMethod.POST)
    public JSONObject getOrderListByUserIdWithPage(@RequestParam(value = "start",defaultValue = "2017-01-01 00:00:00")String startDate,
                                                   @RequestParam(value = "end",defaultValue = "2050-01-01 00:00:00")String endDate,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        Page<Order> orders = null;
        try {
            orders = orderService.getOrderListByUserAndTimeWithPage(hostHolder.getAdmin().getId(),startDate,endDate,pageNum-1,pageSize);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,orders);
    }


    @RequestMapping(value = "deleteOrder",method = RequestMethod.POST)
    public JSONObject deleteOrder(@RequestParam(value = "id") String id){
        orderService.deleteOrderById(id);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,"ok");
    }

    @Authority
    @RequestMapping(value = "getOrderByOrderId", method = RequestMethod.POST)
    public JSONObject getOrderByOrderId(@RequestParam(value = "orderId") String orderId){
        Order order = orderService.getOrderByOrderId(orderId);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,order);
    }

    @Authority
    @RequestMapping(value = "getOrderDetailByOrderId", method = RequestMethod.POST)
    public JSONObject getOrderDetailByOrderId(@RequestParam(value = "orderId") String orderId){
        List<OrderDetail> details = orderService.getOrderDetailByOrderId(orderId);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,details);
    }

    //完成支付,改变订单状态为已支付
    @Authority
    @RequestMapping(value = "finishPay",method = RequestMethod.POST)
    public JSONObject finishPay(@RequestParam("id")String orderId){
        String result = orderService.finishPay(orderId);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,result);
    }

    //发货，填写物流号
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "deliverProducts",method = RequestMethod.POST)
    public JSONObject finishPay(@RequestParam("id")String id,
                                @RequestParam("courierNumber")String courierNumber){
        String result = orderService.deliverProducts(id,courierNumber);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,result);
    }


}
