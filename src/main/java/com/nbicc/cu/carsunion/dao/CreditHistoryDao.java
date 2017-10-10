package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.CreditHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditHistoryDao extends JpaRepository<CreditHistory,String> {
    List<CreditHistory> findByUserId(String userId);

    List<CreditHistory> findByUserIdAndAndSource(String userId, int source);
}
