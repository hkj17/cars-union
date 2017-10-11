package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Merchant;
import com.nbicc.cu.carsunion.model.Order;
import com.nbicc.cu.carsunion.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;

/**
 * Created by bigmao on 2017/8/28.
 */
public interface OrderDao extends JpaRepository<Order,String>,PagingAndSortingRepository<Order,String> {

    Order findByOrderIdAndDelFlag(String orderId,int delFlag);

    Page<Order> findAllByUserAndDatetimeBetweenAndDelFlag(User user, Date start, Date end, int delFlag, Pageable pageable);

    Page<Order> findAllByUserAndDatetimeBetweenAndStatusAndDelFlag(User user, Date start, Date end, int status, int delFlag, Pageable pageable);

    Page<Order> findAllByMerchantAndDatetimeBetweenAndDelFlag(Merchant byId, Date start, Date end, int delFlag, Pageable pageable);

}
