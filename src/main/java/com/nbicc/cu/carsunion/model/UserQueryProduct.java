package com.nbicc.cu.carsunion.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class UserQueryProduct {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(name = "query_title")
    private String queryTitle;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "oem_code")
    private String oemCode;

    @Column(name = "deliver_by")
    private Date deliverBy;

    @Column(name = "need_receipt")
    private boolean needRecipt;

    @Column(name = "address")
    private String address;

    @Column(name = "query_time")
    private Date queryTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getQueryTitle() {
        return queryTitle;
    }

    public void setQueryTitle(String queryTitle) {
        this.queryTitle = queryTitle;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOemCode() {
        return oemCode;
    }

    public void setOemCode(String oemCode) {
        this.oemCode = oemCode;
    }

    public Date getDeliverBy() {
        return deliverBy;
    }

    public void setDeliverBy(Date deliverBy) {
        this.deliverBy = deliverBy;
    }

    public boolean getNeedRecipt() {
        return needRecipt;
    }

    public void setNeedRecipt(boolean needRecipt) {
        this.needRecipt = needRecipt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(Date queryTime) {
        this.queryTime = queryTime;
    }
}
