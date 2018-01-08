package com.nbicc.cu.carsunion.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class UserSignHistory {

    @Id
    @Column
    @GeneratedValue
    private long id;

    @Column
    private String userId;

    @Column
    private Date date;

    @Column
    private int days;

    @Column
    private int addCredit;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getAddCredit() {
        return addCredit;
    }

    public void setAddCredit(int addCredit) {
        this.addCredit = addCredit;
    }
}
