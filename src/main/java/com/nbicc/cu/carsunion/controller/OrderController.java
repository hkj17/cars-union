package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.Authority;
import com.nbicc.cu.carsunion.constant.AuthorityType;
import com.nbicc.cu.carsunion.enumtype.ResponseType;
import com.nbicc.cu.carsunion.model.*;
import com.nbicc.cu.carsunion.service.OrderService;
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
    private OrderService orderService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(value = "addToShoppingCart", method = RequestMethod.POST)
    public JSONObject addToShoppingCart(@RequestParam(value = "productId") String productId,
                                        @RequestParam(value = "quantity") int quantity) {
        String userId = hostHolder.getAdmin().getId();
        boolean state = orderService.addProductToShoppingCart(userId, productId, quantity);
        if (state) {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "操作失败",null);
        }
    }

    @RequestMapping(value = "deleteFromShoppingCart", method = RequestMethod.POST)
    public JSONObject deleteFromShoppingCart(@RequestBody JSONObject json) {
        String userId = hostHolder.getAdmin().getId();
        orderService.deleteFromShoppingCart(userId, json.getObject("productIdList", List.class));
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",null);
    }

    @RequestMapping(value = "modifyShoppingCart", method = RequestMethod.POST)
    public JSONObject modifyShoppingCart(@RequestBody JSONObject json) {
        String userId = hostHolder.getAdmin().getId();
        orderService.modifyShoppingCart(userId, json.getObject("productIdMap", Map.class));
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",null);
    }

    @RequestMapping(value = "getShoppingCartList", method = RequestMethod.POST)
    public JSONObject getShoppingCartList() {
        String userId = hostHolder.getAdmin().getId();
        List<ShoppingCart> shoppingCartList = orderService.getShoppingCartList(userId);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "返回成功",shoppingCartList);
    }

    @RequestMapping(value = "addOrder", method = RequestMethod.POST)
    public JSONObject addOrder(@RequestBody JSONObject json) {
        String merchantId = json.getString("merchantId");
        List<Map> productList = json.getObject("product", List.class);
        String addressId = json.getString("addressId");
        Boolean isOrderFromSc = json.getBoolean("isFromSc");
        if(isOrderFromSc==null){
            isOrderFromSc = false;
        }
        Order newOrder = orderService.addOrder(hostHolder.getAdmin().getId(), merchantId, addressId, productList,isOrderFromSc);
        if (newOrder != null) {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",newOrder);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "操作失败",null);
        }
    }

    //带分页的用户订单列表
    @RequestMapping(value = "getOrderList", method = RequestMethod.POST)
    public JSONObject getOrderListByUserIdWithPage(@RequestParam(value = "start", defaultValue = "2017-01-01 00:00:00") String startDate,
                                                   @RequestParam(value = "end", defaultValue = "2050-01-01 00:00:00") String endDate,
                                                   @RequestParam(value = "status",defaultValue = "-1") int status,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page<Order> orders = null;
        try {
            orders = orderService.getOrderListByUserAndTimeWithPage(hostHolder.getAdmin().getId(), startDate, endDate,status,
                    pageNum - 1, pageSize);
            for(Order order : orders){
                order.setUser(null);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return CommonUtil.response(ResponseType.REQUEST_FAIL,"日期格式错误!",null);
        }
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "返回成功",orders);
    }





    @RequestMapping(value = "deleteOrder", method = RequestMethod.POST)
    public JSONObject deleteOrder(@RequestParam(value = "orderId") String orderId) {
        //检查订单是否属于该用户
        if(orderService.checkOrderBelongToUser(hostHolder.getAdmin().getId(),orderId)) {
            orderService.deleteOrderById(orderId);
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",null);
        }else{
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "orderId is wrong!",null);
        }
    }

    //todo
    @Authority(value = AuthorityType.NoValidate)
    @RequestMapping(value = "getOrderByOrderId", method = RequestMethod.POST)
    public JSONObject getOrderByOrderId(@RequestParam(value = "orderId") String orderId) {
        Order order = orderService.getOrderByOrderId(orderId);
        if (order == null) {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "订单未查到",null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "查询成功", order);
        }
    }

    @Authority
    @RequestMapping(value = "getOrderDetailByOrderId", method = RequestMethod.POST)
    public JSONObject getOrderDetailByOrderId(@RequestParam(value = "orderId") String orderId) {
        List<OrderDetail> details = orderService.getOrderDetailByOrderId(orderId);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "查询成功", details);
    }

    //发货，填写物流号
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "deliverProducts", method = RequestMethod.POST)
    public JSONObject finishPay(@RequestParam("id") String id,
                                @RequestParam("courierNumber") String courierNumber) {
        String result = orderService.deliverProducts(id, courierNumber);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",result);
    }

    @Authority(value = AuthorityType.MerchantValidate)
    @PostMapping("/setServiceTime")
    public JSONObject setServiceTime(@RequestParam("orderId") String orderId,
                                     @RequestParam("serviceTime") String serviceTime) {
        Order order = orderService.setServiceTime(orderId,serviceTime);
        if (order == null) {
            return CommonUtil.response(ResponseType.REQUEST_FAIL,"查不到订单或时间格式错误",null);
        }else {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"查询成功",order);
        }
    }
}
