package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.UserPresentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserPresentHistoryDao extends JpaRepository<UserPresentHistory,Long>, PagingAndSortingRepository<UserPresentHistory,Long> {
}
