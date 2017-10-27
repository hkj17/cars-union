package com.nbicc.cu.carsunion.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * Created by bigmao on 2017/8/18.
 */
@Entity
public class Merchant {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "region")
    private String region;

    @Column(name = "contact")
    private String contact;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "reg_status")
    private int regStatus;

    @Column(name = "reg_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp regTime;

    @Column(name = "idcard_front")
    private String idcardFront;

    @Column(name = "idcard_back")
    private String idcardBack;

    @Column(name = "license_path")
    private String licensePath;

    @Column(name = "logo")
    private String logo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getRegStatus() {
        return regStatus;
    }

    public void setRegStatus(int regStatus) {
        this.regStatus = regStatus;
    }

    public Timestamp getRegTime(){
        return regTime;
    }

    public void setRegTime(Timestamp regTime){
        this.regTime = regTime;
    }

    public String getIdcardFront() {
        return idcardFront;
    }

    public void setIdcardFront(String idcardFront) {
        this.idcardFront = idcardFront;
    }

    public String getIdcardBack() {
        return idcardBack;
    }

    public void setIdcardBack(String idcardBack) {
        this.idcardBack = idcardBack;
    }

    public String getLicensePath() {
        return licensePath;
    }

    public void setLicensePath(String licensePath) {
        this.licensePath = licensePath;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
