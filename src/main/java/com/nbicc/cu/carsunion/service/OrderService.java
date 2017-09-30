package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.dao.*;
import com.nbicc.cu.carsunion.model.*;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    private ShoppingCartDao shoppingCartDao;

    @Autowired
    @PersistenceContext
    private EntityManager em;

    private Logger logger = Logger.getLogger(OrderService.class);

    @Transactional
    public Order addOrder(String userId, String merchantId, String addressId, List<Map> productList) {
        String orderId = generateOrderId();
        BigDecimal totalMoney = new BigDecimal(0);
        for (Map map : productList) {
            String id = UUID.randomUUID().toString().replace("-", "");
            String productId = (String) map.get("productId");
            Product product = productDao.findByIdAndDelFlag(productId, 0);
            int count = (int) map.get("count");
            BigDecimal money = product.getPrice().multiply(BigDecimal.valueOf(count));
            totalMoney = totalMoney.add(money);
            OrderDetail od = new OrderDetail(id, orderId, product, count, money);
            orderDetailDao.save(od);
        }
        String id = UUID.randomUUID().toString().replace("-", "");
        User user = userDao.findById(userId);
        Date date = new Date();
        BigDecimal discount = BigDecimal.valueOf(Double.valueOf("0.90"));  //todo 根据用户信用对应折扣
        BigDecimal realMoney = totalMoney.multiply(discount);
        Merchant merchant = null;
        Address address = null;
        if (!CommonUtil.isNullOrEmpty(merchantId)) {
            merchant = merchantDao.findById(merchantId);
        }
        if (!CommonUtil.isNullOrEmpty(addressId)) {
            address = addressDao.findOne(addressId);
        }
        Order order = new Order(id, orderId, user, date, totalMoney, 0.9, realMoney, merchant, 0, "aa", address, null);
        return orderDao.save(order);
    }

    //生成20位订单号
    private String generateOrderId() {
        Date curDate = new Date();
        String time = String.valueOf(curDate.getTime()).substring(3);
        String random = String.valueOf((int) (Math.random() * 900000 + 100000));
        return "0010" + time.substring(0, 5) + random + time.substring(5);
    }

    public Page<Order> getOrderListByUserAndTimeWithPage(String userId, String startDate, String endDate, int pageNum, int pageSize) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);
        Sort sort = new Sort(Sort.Direction.DESC, "datetime");
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);
        Page<Order> lists = orderDao.findAllByUserAndDatetimeBetweenAndDelFlag(userDao.findById(userId), start, end, 0, pageable);
        return lists;
    }

    public Page<Order> getOrderListByMerchantAndTimeWithPage(String userId, String startDate, String endDate, int pageNum, int pageSize) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);
        Sort sort = new Sort(Sort.Direction.DESC, "datetime");
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);
        Page<Order> lists = orderDao.findAllByMerchantAndDatetimeBetweenAndDelFlag(merchantDao.findById(userId), start, end, 0, pageable);
        return lists;
    }

    public void deleteOrderById(String id) {
        Order order = orderDao.findByOrderIdAndDelFlag(id, 0);
        order.setDelFlag(1);
        orderDao.save(order);
    }

    public Order getOrderByOrderId(String orderId) {
        return orderDao.findByOrderIdAndDelFlag(orderId, 0);
    }

    public List<OrderDetail> getOrderDetailByOrderId(String orderId) {
        return orderDetailDao.findByOrderId(orderId);
    }

    public String finishPay(String orderId) {
        Order order = orderDao.findByOrderIdAndDelFlag(orderId, 0);
        if (order == null) {
            throw new RuntimeException("order not exist!");
        }
        if (order.getStatus() == 0) {
            order.setStatus(1);
        } else {
            throw new RuntimeException("order status is not 0!");
        }
        orderDao.save(order);
        return "ok";
    }

    public String deliverProducts(String orderId, String courierNumber) {
        Order order = orderDao.findByOrderIdAndDelFlag(orderId, 0);
        if (order == null) {
            throw new RuntimeException("order not exist!");
        }
        if (order.getStatus() == 1) {
            order.setStatus(2);
            order.setCourierNumber(courierNumber);
        } else {
            throw new RuntimeException("order status is not 1!");
        }
        orderDao.save(order);
        return "ok";
    }

    @Transactional
    public boolean addProductToShoppingCart(String userId, String productId, int quantity) {
        User user = userDao.findById(userId);
        Product product = productDao.findByIdAndDelFlag(productId, 0);
        if (user == null || product == null) {
            return false;
        }
        ShoppingCart cart = shoppingCartDao.findByUserAndProduct(user, product);
        if (cart != null) {
            cart.setQuantity(cart.getQuantity() + quantity);
        } else {
            cart = new ShoppingCart();
            cart.setId(CommonUtil.generateUUID32());
            cart.setUser(user);
            cart.setProduct(product);
            cart.setQuantity(quantity);
        }
        cart.setCreatedAt(new Date());
        shoppingCartDao.save(cart);
        return true;
    }

    @Transactional
    public boolean deleteFromShoppingCart(String userId, List<String> productIdList) {
        List<ShoppingCart> shoppingCartList = shoppingCartDao.findByUserAndProductIdIn(userId, productIdList);
        shoppingCartDao.deleteInBatch(shoppingCartList);
        return true;
    }

    public boolean modifyShoppingCart(String userId, Map productMap) {
        User user = userDao.findById(userId);
        List<ShoppingCart> shoppingCartList = shoppingCartDao.findByUser(user);
        Iterator<ShoppingCart> shoppingCartIterator = shoppingCartList.iterator();
        while (shoppingCartIterator.hasNext()) {
            ShoppingCart next = shoppingCartIterator.next();
            if (next.getProduct() == null) {
                shoppingCartIterator.remove();
                continue;
            } else {
                Object obj = productMap.get(next.getProduct().getId());
                if (obj == null) {
                    shoppingCartIterator.remove();
                    continue;
                } else {
                    next.setQuantity((int) obj);
                }
            }
        }
        shoppingCartDao.save(shoppingCartList);
        return true;
    }

    public List<ShoppingCart> getShoppingCartList(String userId) {
        User user = userDao.findById(userId);
        return shoppingCartDao.findByUser(user);
    }
}
