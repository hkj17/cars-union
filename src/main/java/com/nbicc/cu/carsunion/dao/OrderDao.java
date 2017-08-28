package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bigmao on 2017/8/28.
 */
public interface OrderDao extends JpaRepository<Order,String>{
}
