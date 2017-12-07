package com.nbicc.cu.carsunion.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class VipLevel {

    @Column(name = "credit_lower")
    private int creditLower;

    @Column(name = "credit_upper")
    private int creditUpper;

    @Id
    @Column(name = "vip_level")
    private int vipLevel;

    @Column(name = "discount")
    private double discount;

    @Column
    private String vipName;

    public int getCreditLower(){
        return creditLower;
    }

    public void setCreditLower(int creditLower){
        this.creditLower = creditLower;
    }

    public int getCreditUpper(){
        return creditUpper;
    }

    public void setCreditUpper(int creditUpper){
        this.creditUpper = creditUpper;
    }

    public int getVipLevel(){
        return vipLevel;
    }

    public void setVipLevel(int vipLevel){
        this.vipLevel = vipLevel;
    }

    public double getDiscount(){
        return discount;
    }

    public void setDiscount(double discount){
        this.discount = discount;
    }

    public String getVipName() {
        return vipName;
    }

    public void setVipName(String vipName) {
        this.vipName = vipName;
    }
}
