package com.nbicc.cu.carsunion.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class CreditHistory {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "credit")
    private int credit;

    @Column(name = "source")
    private int source;

    @Column
    private Date date;

    public CreditHistory() {
    }

    public CreditHistory(String id, String userId, String orderId, int credit, int source,Date date) {
        this.id = id;
        this.userId = userId;
        this.orderId = orderId;
        this.credit = credit;
        this.source = source;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
