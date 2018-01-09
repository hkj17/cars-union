package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.SwUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SwUpdateDao extends JpaRepository<SwUpdate,Long> {

    @Query(value = "select * from sw_update u order by u.upload_time limit 1", nativeQuery = true)
    SwUpdate findLatestUpdate();

    @Query(value = "select u.version from sw_update u order by u.upload_time limit 1", nativeQuery = true)
    SwUpdate findLatestVersion();
}
