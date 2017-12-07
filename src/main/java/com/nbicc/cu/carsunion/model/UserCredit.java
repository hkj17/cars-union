package com.nbicc.cu.carsunion.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserCredit {

    @Id
    @Column
    private String userId;

    @Column
    private int totalCredit;

    @Column
    private int shoppingCredit;

    @Column
    private int signCredit;

    @Column
    private int usedCredit;

    public UserCredit() {
    }

    public UserCredit(String userId, int totalCredit, int shoppingCredit, int signCredit, int usedCredit) {
        this.userId = userId;
        this.totalCredit = totalCredit;
        this.shoppingCredit = shoppingCredit;
        this.signCredit = signCredit;
        this.usedCredit = usedCredit;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(int totalCredit) {
        this.totalCredit = totalCredit;
    }

    public int getShoppingCredit() {
        return shoppingCredit;
    }

    public void setShoppingCredit(int shoppingCredit) {
        this.shoppingCredit = shoppingCredit;
    }

    public int getSignCredit() {
        return signCredit;
    }

    public void setSignCredit(int signCredit) {
        this.signCredit = signCredit;
    }

    public int getUsedCredit() {
        return usedCredit;
    }

    public void setUsedCredit(int usedCredit) {
        this.usedCredit = usedCredit;
    }
}
