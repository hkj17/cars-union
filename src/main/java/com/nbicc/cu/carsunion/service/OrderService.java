package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.dao.*;
import com.nbicc.cu.carsunion.model.*;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by bigmao on 2017/8/28.
 */
@Service
public class OrderService {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderDetailDao orderDetailDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MerchantDao merchantDao;
    @Autowired
    private AddressDao addressDao;

    @Autowired
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Order addOrder(String userId, String merchantId, String addressId, List<Map> productList) {
        String orderId = generateOrderId();
        BigDecimal totalMoney = new BigDecimal(0);
        for(Map map : productList){
            String id = UUID.randomUUID().toString().replace("-","");
            String productId = (String) map.get("productId");
            Product product = productDao.findOne(productId);
            int count = (int) map.get("count");
            BigDecimal money = product.getPrice().multiply(BigDecimal.valueOf(count));
            totalMoney = totalMoney.add(money);
            OrderDetail od = new OrderDetail(id,orderId,product,count,money);
            orderDetailDao.save(od);
        }
        String id = UUID.randomUUID().toString().replace("-","");
        User user = userDao.findById(userId);
        Date date = new Date();
        BigDecimal discount = BigDecimal.valueOf(Double.valueOf("0.90"));  //todo 根据用户信用对应折扣
        BigDecimal realMoney = totalMoney.multiply(discount);
        Merchant merchant = null;
        Address address = null;
        if(!CommonUtil.isNullOrEmpty(merchantId)){
            merchant = merchantDao.findById(merchantId);
        }
        if(!CommonUtil.isNullOrEmpty(addressId)){
            address = addressDao.findOne(addressId);
        }
        Order order = new Order(id,orderId,user,date,totalMoney,0.9,realMoney,merchant,0,"aa",address,null);
        return orderDao.save(order);
    }

    //生成20位订单号
    private String generateOrderId() {
        Date curDate = new Date();
        String time = String.valueOf(curDate.getTime()).substring(3);
        String random = String.valueOf((int) (Math.random() * 900000 + 100000));
        return "0010" + time.substring(0,5) + random + time.substring(5);
    }

    public List<Order> getOrderListByUserAndTime(String userId, String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<Integer, Object> paramMap = new HashMap<Integer, Object>();
        String sql = "from Order o where o.user.id = ?1";
        paramMap.put(1,userId);
        sql = setSqlDate(sql,paramMap,2,startDate,endDate);

        Query query = em.createQuery(sql);
        for (int p = 1; p <= paramMap.size(); p++) {
            query.setParameter(p, paramMap.get(p));
        }
        return query.getResultList();
    }

    public List<Order> getOrderListByMerchantAndTime(String merchantId, String startDate, String endDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<Integer, Object> paramMap = new HashMap<Integer, Object>();
        String sql = "from Order o where o.merchant.id = ?1";
        paramMap.put(1,merchantId);
        sql = setSqlDate(sql,paramMap,2,startDate,endDate);

        Query query = em.createQuery(sql);
        for (int p = 1; p <= paramMap.size(); p++) {
            query.setParameter(p, paramMap.get(p));
        }
        return query.getResultList();
    }

    @Transactional
    public void delete(String id) {
        Order order = orderDao.findOne(id);
        List<OrderDetail> lists = orderDetailDao.findByOrderId(order.getOrderId());
        orderDetailDao.delete(lists);
        orderDao.delete(order);
    }

    public Order getOrderByOrderId(String orderId){
        return orderDao.findByOrderId(orderId);
    }

    public List<OrderDetail> getOrderDetailByOrderId(String orderId) {
        return orderDetailDao.findByOrderId(orderId);
    }

    public String finishPay(String orderId) {
        Order order = orderDao.findOne(orderId);
        if(order == null){
            throw new RuntimeException("order not exist!");
        }
        if(order.getStatus() == 0){
            order.setStatus(1);
        }else{
            throw new RuntimeException("order status is not 0!");
        }
        orderDao.save(order);
        return "ok";
    }

    public String deliverProducts(String orderId, String courierNumber) {
        Order order = orderDao.findOne(orderId);
        if(order == null){
            throw new RuntimeException("order not exist!");
        }
        if(order.getStatus() == 1){
            order.setStatus(2);
            order.setCourierNumber(courierNumber);
        }else{
            throw new RuntimeException("order status is not 1!");
        }
        orderDao.save(order);
        return "ok";
    }

    private String setSqlDate(String sql, Map<Integer,Object> paramMap, int i, String startDate, String endDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!CommonUtil.isNullOrEmpty(startDate)) {
            startDate = startDate + " 00:00:00";
            sql += " and o.datetime>=?"+i;
            try {
                paramMap.put(i++, sdf.parse(startDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (!CommonUtil.isNullOrEmpty(endDate)) {
            endDate = endDate + " 23:59:59";
            sql += " and o.datetime<=?"+i;
            try {
                paramMap.put(i++, sdf.parse(endDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return sql;
    }
}
