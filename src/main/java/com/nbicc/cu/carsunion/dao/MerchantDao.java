package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by bigmao on 2017/8/18.
 */
public interface MerchantDao extends JpaRepository<Merchant,String>,PagingAndSortingRepository<Merchant,String> {

    Merchant findById(String id);

    Merchant findByPhone(String phone);

    Page<Merchant> findByRegStatus(int status, Pageable pageable);
}
