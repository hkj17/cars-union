package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Present;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PresentDao extends JpaRepository<Present,Long>,PagingAndSortingRepository<Present,Long> {
    public Present findByIdAndDelFlag(long id,boolean delFlag);

    public Page<Present> findByDelFlag(boolean delFlag,Pageable pageable);

    public Page<Present> findByDelFlagAndOnSale(boolean delFlag, boolean onSale,Pageable pageable);
}
