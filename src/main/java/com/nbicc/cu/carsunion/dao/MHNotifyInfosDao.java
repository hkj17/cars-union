package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.MHNotifyInfos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MHNotifyInfosDao extends JpaRepository<MHNotifyInfos,Long>{
    Page<MHNotifyInfos> findByUserId(String userId, Pageable pageable);
}
