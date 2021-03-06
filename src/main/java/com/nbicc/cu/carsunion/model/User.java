package com.nbicc.cu.carsunion.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class User {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "contact")
    private String contact;

    @Column(name = "region")
    private String region;

    @Column(name = "portrait_path")
    private String portraitPath;

    @Column(name = "recommend")
    private String recommend;

    @Column
    private String shareCode;


    @OneToMany(mappedBy = "user")
    private List<Address> addressList;

    @ManyToMany
    @JoinTable(name = "user_vehicle_relationship", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "vehicle_id"))
    private Set<Vehicle> vehicles;

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

    public String getNickname(){
        return nickname;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPortraitPath(){
        return portraitPath;
    }

    public void setPortraitPath(String portraitPath){
        this.portraitPath = portraitPath;
    }

    public String getRecommend(){
        return recommend;
    }

    public void setRecommend(String recommend){
        this.recommend = recommend;
    }

    public List<Address> getAddressList(){
        return addressList;
    }

    public void setAddressList(List<Address> addressList){
        this.addressList = addressList;
    }

    @JsonIgnore
    public Set<Vehicle> getVehicles(){
        return vehicles;
    }

    public void setVehicles(Set<Vehicle> vehicles){
        this.vehicles = vehicles;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }
}
