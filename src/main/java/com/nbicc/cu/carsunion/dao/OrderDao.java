package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by bigmao on 2017/8/28.
 */
public interface OrderDao extends JpaRepository<Order,String>{

    Order findByOrderId(String orderId);
}
