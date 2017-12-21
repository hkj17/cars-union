package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.CreditHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;


public interface CreditHistoryDao extends JpaRepository<CreditHistory,String>,PagingAndSortingRepository<CreditHistory,String> {
    Page<CreditHistory> findByUserIdOrderByDateDesc(String userId, Pageable pageable);

    Page<CreditHistory> findByUserIdAndSourceOrderByDateDesc(String userId, int source,Pageable pageable);
}
