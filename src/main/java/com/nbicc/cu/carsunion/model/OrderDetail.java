package com.nbicc.cu.carsunion.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by bigmao on 2017/8/28.
 */
@Entity
public class OrderDetail {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order userOrder;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int count;

    @Column(name = "total_money")
    private BigDecimal totalMoney;

    public OrderDetail() {
    }

    public OrderDetail(String id, Product product, int count, BigDecimal totalMoney) {
        this.id = id;
        this.product = product;
        this.count = count;
        this.totalMoney = totalMoney;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Order getUserOrder() {
        return userOrder;
    }

    public void setUserOrder(Order userOrder) {
        this.userOrder = userOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }
}
