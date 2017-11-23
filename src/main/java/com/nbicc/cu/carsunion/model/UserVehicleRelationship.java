package com.nbicc.cu.carsunion.model;

import javax.persistence.*;

@Entity
@Table(name = "user_vehicle_relationship")
public class UserVehicleRelationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "is_bind_mh")
    private Boolean isBindMh;

    @Column(name = "mh_vehicle_id")
    private String mhVehicleId;

    @Column(name = "mh_hw_id")
    private String mhHwId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Boolean getIsDefault(){
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault){
        this.isDefault = isDefault;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Boolean getIsBindMh() {
        return isBindMh;
    }

    public void setIsBindMh(Boolean bindMh) {
        isBindMh = bindMh;
    }

    public String getMhVehicleId() {
        return mhVehicleId;
    }

    public void setMhVehicleId(String mhVehicleId) {
        this.mhVehicleId = mhVehicleId;
    }

    public String getMhHwId() {
        return mhHwId;
    }

    public void setMhHwId(String mhHwId) {
        this.mhHwId = mhHwId;
    }
}
