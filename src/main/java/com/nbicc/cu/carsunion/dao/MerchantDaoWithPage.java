package com.nbicc.cu.carsunion.dao;

import com.nbicc.cu.carsunion.model.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MerchantDaoWithPage extends PagingAndSortingRepository<Merchant,String> {

    Page<Merchant> findByRegStatus(int status, Pageable pageable);
}
