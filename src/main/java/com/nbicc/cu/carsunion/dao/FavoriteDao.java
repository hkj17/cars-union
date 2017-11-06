package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Favorite;
import com.nbicc.cu.carsunion.model.Product;
import com.nbicc.cu.carsunion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoriteDao extends JpaRepository<Favorite,String> {
    @Query("select f from Favorite f where f.user.id = ?1 and f.favorite = ?2 order by f.createdAt desc")
    List<Favorite> findByUserAndFavorite(String userId, boolean isFavorite);

    @Query("select f from Favorite f where f.user.id = ?1 and f.browsed = ?2 order by f.lastVisited desc")
    List<Favorite> findByUserAndBrowsed(String userId, boolean isBrowsed);

    Favorite findByUserAndProduct(User user, Product product);

    @Query("select f from Favorite f where f.user.id = ?1 and f.product.id in ?2")
    List<Favorite> findByUserAndProductIn(String userId, List<String> productIdList);
}
