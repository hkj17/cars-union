package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.UserPresentHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserPresentHistoryDao extends JpaRepository<UserPresentHistory,Long>, PagingAndSortingRepository<UserPresentHistory,Long> {

    Page<UserPresentHistory> findByUserId(String userId, Pageable pageable);

    Page<UserPresentHistory> findAll(Pageable pageable);

    Page<UserPresentHistory> findByPresentId(long present, Pageable pageable);
}
