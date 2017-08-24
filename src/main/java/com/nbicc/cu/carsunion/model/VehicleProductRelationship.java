package com.nbicc.cu.carsunion.model;

import javax.persistence.*;

/**
 * Created by bigmao on 2017/8/24.
 */
@Entity
@Table(name = "vehicle_product_relationship")
public class VehicleProductRelationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public VehicleProductRelationship() {
    }

    public VehicleProductRelationship(Vehicle vehicle, Product product) {
        this.vehicle = vehicle;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
