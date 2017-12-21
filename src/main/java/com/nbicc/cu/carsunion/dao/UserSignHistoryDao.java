package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.UserSignHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface UserSignHistoryDao extends JpaRepository<UserSignHistory,Long>, PagingAndSortingRepository<UserSignHistory,Long> {

    UserSignHistory findFirstByUserIdOrderByDateDesc(String userId);

    List<UserSignHistory> findByUserIdAndDateBetween(String userId, Date start, Date end);

    Page<UserSignHistory> findByUserIdAndDateBetweenOrderByDateDesc(String userId, Date start, Date end, Pageable pageable);
}
