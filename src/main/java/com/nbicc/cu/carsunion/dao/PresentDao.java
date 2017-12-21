package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Present;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PresentDao extends JpaRepository<Present,Long>,PagingAndSortingRepository<Present,Long> {
    public Present findByIdAndDelFlag(long id,boolean delFlag);

    public Page<Present> findByDelFlag(boolean delFlag,Pageable pageable);

    public Page<Present> findByDelFlagAndOnSale(boolean delFlag, boolean onSale,Pageable pageable);

    public Present findByIdAndOnSaleAndDelFlag(long id, boolean onSale, boolean delFlag);

    @Query("update Present set totalQuantity = ?1 where id = ?2 and totalQuantity = ?3 and delFlag = false and onSale = true ")
    @Modifying
    public int updateStock(int newValue,long presentId, int oldValue);

    @Query("update Present set totalQuantity = ?1 where id = ?2 and totalQuantity = ?3 and delFlag = false")
    @Modifying
    public int updateStockAdmin(int newValue,long presentId, int oldValue);
}
