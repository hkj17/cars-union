package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.UserSignHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface UserSignHistoryDao extends JpaRepository<UserSignHistory,Long>{

    UserSignHistory findFirstByUserIdOrderByDateDesc(String userId);

    List<UserSignHistory> findByUserIdAndDateBetween(String userId, Date start, Date end);
}
