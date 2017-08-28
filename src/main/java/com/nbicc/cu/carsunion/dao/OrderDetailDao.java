package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bigmao on 2017/8/28.
 */
public interface OrderDetailDao extends JpaRepository<OrderDetail,String> {

}
