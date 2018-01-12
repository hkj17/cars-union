package com.nbicc.cu.carsunion.model;

public class MHNotifyModel {

    private String type;
    private String subType;
    private String object;
    private String content;
    private String time;
    private String lon;
    private String lat;

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

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
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
        return "MHNotifyModel{" +
                "type='" + type + '\'' +
                ", subType='" + subType + '\'' +
                ", object='" + object + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", lon='" + lon + '\'' +
                ", lat='" + lat + '\'' +
                '}';
    }
}
