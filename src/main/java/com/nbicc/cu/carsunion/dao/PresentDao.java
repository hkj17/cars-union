package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Present;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PresentDao extends JpaRepository<Present,Long>,PagingAndSortingRepository<Present,Long> {

    Present findById(long id);

    @Query("update Present set totalQuantity = ?1 where id = ?2 and totalQuantity = ?3 and delFlag = 0 and onSale = true ")
    @Modifying
    int updateStock(int newValue,long presentId, int oldValue);
}
