package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.VipLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VipLevelDao extends JpaRepository<VipLevel,Integer> {

    @Query("select v from VipLevel v where v.creditLower <= ?1 and v.creditUpper > ?1")
    VipLevel findVipLevelByRange(int credit);
}
