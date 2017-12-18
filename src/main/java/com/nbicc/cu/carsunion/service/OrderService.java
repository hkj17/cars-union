package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.constant.ParameterValues;
import com.nbicc.cu.carsunion.dao.*;
import com.nbicc.cu.carsunion.enumtype.OrderStatus;
import com.nbicc.cu.carsunion.model.*;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private CreditHistoryDao creditHistoryDao;
    @Autowired
    private VipLevelDao vipLevelDao;
    @Autowired
    private UserCreditDao userCreditDao;

    private static Logger logger = LogManager.getLogger(OrderService.class);

    /**
     * 新增订单
     *
     * @param userId
     * @param merchantId
     * @param addressId
     * @param productList
     * @param isFromSc
     * @return
     */
    @Transactional
    public Order addOrder(String userId, String merchantId, String addressId, List<Map> productList, boolean isFromSc) {
        String orderId = generateOrderId();
        BigDecimal totalMoney = new BigDecimal(0);
        List<String> productIdList = new ArrayList<String>();
        List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();

        for (Map map : productList) {
            String id = UUID.randomUUID().toString().replace("-", "");
            String productId = (String) map.get("productId");
            productIdList.add(productId);
            Product product = productDao.findByIdAndDelFlag(productId, 0);
            int count = (int) map.get("count");
            BigDecimal money = product.getPrice().multiply(BigDecimal.valueOf(count));
            totalMoney = totalMoney.add(money);
            orderDetailList.add(new OrderDetail(id, product, count, money));
        }

        String id = UUID.randomUUID().toString().replace("-", "");
        User user = userDao.findById(userId);
        Date date = new Date();
        double discount = getDiscount(userCreditDao.findByUserId(userId).getTotalCredit());
        BigDecimal realMoney = totalMoney.multiply(BigDecimal.valueOf(discount));
        Merchant merchant = null;
        Address address = null;
        if (!CommonUtil.isNullOrEmpty(merchantId)) {
            merchant = merchantDao.findById(merchantId);
        }
        if (!CommonUtil.isNullOrEmpty(addressId)) {
            address = addressDao.findOne(addressId);
        }

        Order order = new Order(id, orderId, user, date, totalMoney, discount, realMoney, merchant, OrderStatus.NOT_PAYED.ordinal(), null, address);
        //从购物车里面删除商品
        if (isFromSc) {
            deleteFromShoppingCart(userId, productIdList);
        }
        for (OrderDetail orderDetail : orderDetailList) {
            orderDetail.setUserOrder(order);
        }
        orderDao.save(order);
        orderDetailDao.save(orderDetailList);
        return orderDao.findByOrderIdAndDelFlag(orderId, 0);
    }

    /**
     * 根据积分获取折扣
     *
     * @param credit
     * @return
     */
    private double getDiscount(int credit) {
        return vipLevelDao.findVipLevelByRange(credit).getDiscount();
    }

    /**
     * 生成20位订单号
     *
     * @return
     */
    private String generateOrderId() {
        Date curDate = new Date();
        String time = String.valueOf(curDate.getTime()).substring(3);
        String random = String.valueOf((int) (Math.random() * 900000 + 100000));
        return "0010" + time.substring(0, 5) + random + time.substring(5);
    }

    /**
     * 查询用户订单列表，按时间/订单状态查询，带分页
     *
     * @param userId
     * @param startDate
     * @param endDate
     * @param status
     * @param pageNum
     * @param pageSize
     * @return
     * @throws ParseException
     */
    public Page<Order> getOrderListByUserAndTimeWithPage(String userId, String startDate, String endDate, int status, int pageNum, int pageSize) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);
        Sort sort = new Sort(Sort.Direction.DESC, "datetime");
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);
        Page<Order> lists = null;
        if (status < 0) {
            lists = orderDao.findAllByUserAndDatetimeBetweenAndDelFlag(userDao.findById(userId), start, end, 0, pageable);
        } else {
            lists = orderDao.findAllByUserAndDatetimeBetweenAndStatusAndDelFlag(userDao.findById(userId), start, end, status, 0, pageable);
        }
        return lists;
    }

    /**
     * 商家查看已付款订单，可按订单状态查询，带分页
     *
     * @param userId
     * @param status
     * @param startDate
     * @param endDate
     * @param pageNum
     * @param pageSize
     * @return
     * @throws ParseException
     */
    public Page<Order> getOrderListByMerchantAndTimeWithPage(String userId, int status, String startDate, String endDate, int pageNum, int pageSize) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);
        Sort sort = new Sort(Sort.Direction.DESC, "datetime");
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);
        Page<Order> lists;
        if (status <= 0) {
            lists = orderDao.findAllByMerchantAndDatetimeBetweenAndStatusIsNot(merchantDao.findById(userId), start, end, 0, pageable);
        } else {
            lists = orderDao.findAllByMerchantAndDatetimeBetweenAndStatus(merchantDao.findById(userId), start, end, status, pageable);
        }
        return lists;
    }

    /**
     * 删除订单，更新删除标记
     *
     * @param orderId
     */
    public void deleteOrderById(String orderId) {
        Order order = orderDao.findByOrderIdAndDelFlag(orderId, 0);
        order.setDelFlag(1);
        orderDao.save(order);
    }

    /**
     * 根据订单号查询
     *
     * @param orderId
     * @return
     */
    public Order getOrderByOrderId(String orderId) {
        return orderDao.findByOrderIdAndDelFlag(orderId, 0);
    }

    /**
     * 查询订单详情
     *
     * @param orderId
     * @return
     */
    public List<OrderDetail> getOrderDetailByOrderId(String orderId) {
        Order order = orderDao.findByOrderIdAndDelFlag(orderId, 0);
        if (order == null) {
            System.out.println("order does not exist");
            return new ArrayList<>();
        }
        System.out.println(order.getOrderId());
        return orderDetailDao.findByUserOrder(order);
    }

    /**
     * 支付完成后一系列操作
     *
     * @param orderId
     * @return
     */
    @Transactional
    public String finishPay(String orderId) {
        Order order = orderDao.findByOrderIdAndDelFlag(orderId, 0);
        if (order == null) {
            logger.info("-------- Order[" + orderId + "]  does not exist!");
            return "order does not exist!";
        }
        if (order.getStatus() == OrderStatus.NOT_PAYED.ordinal()) {
            order.setPayTime(new Date());
            order.setStatus(OrderStatus.PAYED.ordinal());

            List<CreditHistory> creditHistories = new ArrayList<CreditHistory>();
            String userId = order.getUser().getId();
            String recommendorId = order.getUser().getRecommend();
            int credit = order.getRealMoney().intValue();
            CreditHistory self = new CreditHistory(CommonUtil.generateUUID32(), userId, orderId, credit, 0, new Date());
            creditHistories.add(self);

            //用户积分更新
            updateUserCredit(userId, credit);

            if (!CommonUtil.isNullOrEmpty(recommendorId)) {
                CreditHistory firstRecommendor = new CreditHistory(CommonUtil.generateUUID32(), recommendorId, orderId, (int) (credit * ParameterValues.RECOMMENDOR_CREDIT_RATIO), 1, new Date());
                creditHistories.add(firstRecommendor);
                User recommendor = userDao.findById(recommendorId);
                //用户积分更新
                updateUserCredit(recommendorId, (int) (credit * ParameterValues.RECOMMENDOR_CREDIT_RATIO));

                if (!CommonUtil.isNullOrEmpty(recommendor) && !CommonUtil.isNullOrEmpty(recommendor.getRecommend())) {
                    CreditHistory secondRecommendor = new CreditHistory(CommonUtil.generateUUID32(), recommendor.getRecommend(), orderId, (int) (credit * ParameterValues.RECOMMENDOR_CREDIT_RATIO * ParameterValues.RECOMMENDOR_CREDIT_RATIO), 2, new Date());
                    creditHistories.add(secondRecommendor);
                    //用户积分更新
                    updateUserCredit(recommendor.getRecommend(), (int) (credit * ParameterValues.RECOMMENDOR_CREDIT_RATIO * ParameterValues.RECOMMENDOR_CREDIT_RATIO));
                }
            }

            //增加商品的销售量
            List<OrderDetail> details = orderDetailDao.findByUserOrder(order);
            for (OrderDetail detail : details) {
                Product product = productDao.findById(detail.getProduct().getId());
                if (product != null) {
                    product.setSaleNum(product.getSaleNum() + detail.getCount());
                    productDao.save(product);
                }
            }

            creditHistoryDao.save(creditHistories);
            orderDao.save(order);
        } else {
            logger.info("-------- Order[" + orderId + "]  is not ready for payment!");
            return "order is not ready for payment!";
        }
        return "ok";
    }


    /**
     * 用户总积分和消费积分更新
     *
     * @param userId
     * @param credit
     */
    private void updateUserCredit(String userId, int credit) {
        UserCredit userCredit = userCreditDao.findByUserId(userId);
        if (userCredit != null) {
            userCredit.setShoppingCredit(userCredit.getShoppingCredit() + credit);
            userCredit.setTotalCredit(userCredit.getTotalCredit() + credit);
            userCreditDao.save(userCredit);
        }
    }

    /**
     * 填写订单物流号
     *
     * @param orderId
     * @param courierNumber
     * @return
     */
    public String deliverProducts(String orderId, String courierNumber) {
        Order order = orderDao.findByOrderIdAndDelFlag(orderId, 0);
        if (order == null) {
            throw new RuntimeException("order does not exist!");
        }
        if (order.getStatus() == OrderStatus.PAYED.ordinal()) {
            order.setStatus(OrderStatus.DELIVERED.ordinal());
            order.setDeliverTime(new Date());
            order.setCourierNumber(courierNumber);
        } else {
            throw new RuntimeException("order is not payed!");
        }
        orderDao.save(order);
        return "ok";
    }

    /**
     * 添加到购物车
     *
     * @param userId
     * @param productId
     * @param quantity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
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

    /**
     * 从购物车删除商品
     *
     * @param userId
     * @param productIdList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFromShoppingCart(String userId, List<String> productIdList) {
        List<ShoppingCart> shoppingCartList = shoppingCartDao.findByUserAndProductIdIn(userId, productIdList);
        shoppingCartDao.deleteInBatch(shoppingCartList);
        return true;
    }

    /**
     * 修改购物车
     *
     * @param userId
     * @param productMap
     * @return
     */
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

    /**
     * 用户购物车列表
     *
     * @param userId
     * @return
     */
    public List<ShoppingCart> getShoppingCartList(String userId) {
        User user = userDao.findById(userId);
        List<ShoppingCart> shoppingCartList = shoppingCartDao.findByUser(user);
        return shoppingCartList;
    }

    /**
     * 设置订单服务时间
     *
     * @param orderId
     * @param serviceTime
     * @return
     */
    public Order setServiceTime(String orderId, String serviceTime) {
        Date serviceDate;
        try {
            serviceDate = CommonUtil.String2Date(serviceTime);
        } catch (ParseException e) {
            logger.error("SetServiceTime ParseException : " + e.getMessage());
            return null;
        }
        Order order = orderDao.findByOrderIdAndDelFlag(orderId, 0);
        if (order == null) {
            return null;
        } else {
            order.setServiceTime(serviceDate);
            orderDao.save(order);
            return order;
        }
    }

    /**
     * 管理员查看所有订单，包括用户已删除的，按时间查询，可分页
     *
     * @param startDate
     * @param endDate
     * @param status
     * @param pageNum
     * @param pageSize
     * @return
     * @throws ParseException
     */
    public Page<Order> getOrderListByTimeWithPage(String startDate, String endDate, int status, int pageNum, int pageSize) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);
        Sort sort = new Sort(Sort.Direction.DESC, "datetime");
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);
        Page<Order> lists = null;
        if (status < 0) {
            lists = orderDao.findAllByDatetimeBetween(start, end, pageable);
        } else {
            lists = orderDao.findAllByDatetimeBetweenAndStatus(start, end, status, pageable);
        }
        return lists;
    }

    /**
     * 检查订单是否属于用户
     *
     * @param userId
     * @param orderId
     * @return
     */
    public boolean checkOrderBelongToUser(String userId, String orderId) {
        Order order = orderDao.findByOrderIdAndDelFlag(orderId, 0);
        if (order == null) {
            return false;
        }
        return userId.equals(order.getUser().getId());
    }
}
