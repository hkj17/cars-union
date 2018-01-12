package com.nbicc.cu.carsunion.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mh_notify_infos")
public class MHNotifyInfos {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "hw_id")
    private String object;

    @Column
    private String type;

    @Column(name = "sub_type")
    private String subType;

    @Column
    private String content;

    @Column
    private Date time;

    @Column
    private String lon;

    @Column
    private String lat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "MHNotifyInfos{" +
                "id=" + id +
                ", hwId='" + object + '\'' +
                ", type='" + type + '\'' +
                ", subType='" + subType + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", lon='" + lon + '\'' +
                ", lat='" + lat + '\'' +
                '}';
    }
}
