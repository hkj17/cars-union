package com.nbicc.cu.carsunion.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by bigmao on 2017/8/28.
 */
@Entity
public class Order {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "order_id")
    private String orderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "datetime")
    private Date datetime;

    @Column(name = "total_money")
    private BigDecimal totalMoney;

    @Column(name = "discount")
    private double discount;

    @Column(name = "real_money")
    private BigDecimal realMoney;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @Column(name = "status")
    private int status;

    @Column(name = "courier_number")
    private String courierNumber;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    public Order() {
    }

    public Order(String id, String orderId, User user, Date datetime, BigDecimal totalMoney, double discount, BigDecimal realMoney, Merchant merchant, int status, String courierNumber, Address address) {
        this.id = id;
        this.orderId = orderId;
        this.user = user;
        this.datetime = datetime;
        this.totalMoney = totalMoney;
        this.discount = discount;
        this.realMoney = realMoney;
        this.merchant = merchant;
        this.status = status;
        this.courierNumber = courierNumber;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public BigDecimal getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(BigDecimal realMoney) {
        this.realMoney = realMoney;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCourierNumber() {
        return courierNumber;
    }

    public void setCourierNumber(String courierNumber) {
        this.courierNumber = courierNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
