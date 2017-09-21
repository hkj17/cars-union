package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Product;
import com.nbicc.cu.carsunion.model.ShoppingCart;
import com.nbicc.cu.carsunion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShoppingCartDao extends JpaRepository<ShoppingCart, String> {
    List<ShoppingCart> findByUser(User user);

    ShoppingCart findByUserAndProduct(User user, Product product);

    @Query("select sc from ShoppingCart sc where sc.user.id = ?1 and sc.product.id in ?2")
    List<ShoppingCart> findByUserAndProductIdIn(String userId, List<String> productIdList);
}
