package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.ShoppingCart;
import com.nbicc.cu.carsunion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingCartDao extends JpaRepository<ShoppingCart, String> {
    List<ShoppingCart> findByUser(User user);
}
