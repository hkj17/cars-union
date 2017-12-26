package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.User;
import com.nbicc.cu.carsunion.model.UserFeedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UserFeedbackDao extends JpaRepository<UserFeedback,Long>, PagingAndSortingRepository<UserFeedback,Long> {
    Page<UserFeedback> findByUser(User user,Pageable pageable);

    Page<UserFeedback> findAll(Pageable pageable);
}
