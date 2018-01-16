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

    @Column(name = "user_id")
    private String userId;

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

    @Column(name = "plate_num")
    private String plateNum;

    @Transient
    private String typeContent;

    @Transient
    private String subTypeContent;

    @Transient
    private String specificContent;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public String getTypeContent() {
        return typeContent;
    }

    public void setTypeContent(String type) {
        String typeContent;
        if("1".equals(type)){
            typeContent = "告警";
        }else{
            typeContent = "通知";
        }
        this.typeContent = typeContent;
    }

    public String getSubTypeContent() {
        return subTypeContent;
    }

    public void setSubTypeContent(String subType) {
        String subTypeContent;
        switch (subType){
            case "0" : {
                subTypeContent = "尾箱开启提醒";
                if("0".equals(content)){
                    setSpecificContent("尾箱关闭");
                }else {
                    setSpecificContent("尾箱开启");
                }
                break;
            }
            case "1" : subTypeContent = "震动报警";break;
            case "2" : subTypeContent = "未锁车提醒";break;
            case "3" : subTypeContent = "车辆未预约启动";break;
            case "4" : subTypeContent = "急加速报警";break;
            case "5" : subTypeContent = "急减速报警";break;
            case "6" : subTypeContent = "行驶不系安全带";break;
            case "7" : subTypeContent = "变道不打转向灯";break;
            case "8" : subTypeContent = "车辆被盗提醒（车门开启）";break;
            case "9" : subTypeContent = "车辆被盗提醒（尾箱开启）";break;
            case "10" : subTypeContent = "设防成功，小灯未关";break;
            case "11" : subTypeContent = "设防失败，尾箱未关";break;
            case "12" : subTypeContent = "设防失败，车门未关";break;
            case "13" : subTypeContent = "设防成功，车窗未关";break;
            case "14" : subTypeContent = "急转弯报警";break;
            case "15" : {
                subTypeContent = "车轮胎压异常通知";
                String sContent;
                switch (content) {
                    case "0":sContent = "左前轮胎压正常";break;
                    case "1":sContent = "右前轮胎压正常";break;
                    case "2":sContent = "左后轮胎压正常";break;
                    case "3":sContent = "右后轮胎压正常";break;
                    case "4":sContent = "左前轮胎压不正常";break;
                    case "5":sContent = "右前轮胎压不正常";break;
                    case "6":sContent = "左后轮胎压不正常";break;
                    case "7":sContent = "右后轮胎压不正常";break;
                    default:sContent = "";
                }
                setSpecificContent(sContent);
            }break;
            case "16" : {
                subTypeContent = "车门开启提醒";
                String sContent;
                switch (content) {
                    case "0":sContent = "左前门开";break;
                    case "1":sContent = "右前门开";break;
                    case "2":sContent = "左后门开";break;
                    case "3":sContent = "右后门开";break;
                    case "4":sContent = "左前门关";break;
                    case "5":sContent = "右前门关";break;
                    case "6":sContent = "左后门关";break;
                    case "7":sContent = "右后门关";break;
                    default:sContent = "";
                }
                setSpecificContent(sContent);
            }break;
            case "17" : subTypeContent = "车辆设防";break;
            case "18" : subTypeContent = "车辆撤防";break;
            case "19" : subTypeContent = "车辆启动";break;
            case "20" : subTypeContent = "车辆熄火";break;
            case "21" : {
                subTypeContent = "超速提醒";
                setSpecificContent("当前车速为：" + content + " km/h");
            }break;
            case "22" : subTypeContent = "剪线提醒";break;
            case "23" : {
                subTypeContent = "低电压提醒";
                setSpecificContent("当前电压为：" + content + " V");
            }break;
            case "24" : subTypeContent = "车辆已离线";break;
            case "25" : {
                subTypeContent = "车窗状态通知";
                String sContent;
                switch (content) {
                    case "0":sContent = "左前窗开";break;
                    case "1":sContent = "右前窗开";break;
                    case "2":sContent = "左后窗开";break;
                    case "3":sContent = "右后窗开";break;
                    case "4":sContent = "左前窗关";break;
                    case "5":sContent = "右前窗关";break;
                    case "6":sContent = "左后窗关";break;
                    case "7":sContent = "右后窗关";break;
                    default:sContent = "";
                }
                setSpecificContent(sContent);
            }break;
            case "26" : {
                subTypeContent = "疲劳驾驶通知";
                setSpecificContent("您已连续驾驶 " + content + " 小时，请注意休息");
            }break;
            case "27" : {
                subTypeContent = "怠速超时提醒";
                setSpecificContent("怠速时长为： " + content + " 小时");
            }break;
            case "28" : {
                subTypeContent = "转向灯状态通知";
                String sContent;
                switch (content) {
                    case "0":sContent = "左转向灯开";break;
                    case "1":sContent = "右转向灯开";break;
                    case "2":sContent = "左转向灯关";break;
                    case "3":sContent = "右转向灯关";break;
                    default:sContent = "";
                }
                setSpecificContent(sContent);
            }break;
            case "29" : {
                subTypeContent = "小灯状态通知";
                if("0".equals(content)){
                    setSpecificContent("小灯关");
                }else{
                    setSpecificContent("小灯开");
                }
            }break;
            case "30" : {
                subTypeContent = "门锁状态通知";
                if("0".equals(content)){
                    setSpecificContent("开锁");
                }else{
                    setSpecificContent("关锁");
                }
            }break;
            case "31" : {
                subTypeContent = "报警状态";
                setSpecificContent(content);
            }break;
            case "32" : {
                subTypeContent = "点火开关状态";
                if("0".equals(content)){
                    setSpecificContent("关闭");
                }else{
                    setSpecificContent("开启");
                }
            }break;
            case "33" : {
                subTypeContent = "门未关提醒";
                if("0".equals(content)){
                    setSpecificContent("关好");
                }else{
                    setSpecificContent("未关");
                }
            }break;
            case "42" : subTypeContent = "APP点火成功";break;
            case "43" : subTypeContent = "APP点火失败";break;
            case "44" : subTypeContent = "APP熄火成功";break;
            case "45" : subTypeContent = "APP熄火失败";break;
            case "46" : {
                subTypeContent = "车辆变道";
                if("0".equals(content)){
                    setSpecificContent("正常");
                }else{
                    setSpecificContent("连续变道");
                }
            }break;
            case "47" : {
                subTypeContent = "车辆碰撞";
                if("0".equals(content)){
                    setSpecificContent("未产生碰撞");
                }else if("1".equals(content)){
                    setSpecificContent("轻微碰撞");
                }else {
                    setSpecificContent("严重碰撞");
                }
            }break;
            default : subTypeContent = "";
        }

        this.subTypeContent = subTypeContent;
    }

    public String getSpecificContent() {
        return specificContent;
    }

    public void setSpecificContent(String specificContent) {
        this.specificContent = specificContent;
    }

    public void fillContent(){
        setTypeContent(this.type);
        setSubTypeContent(this.subType);
    }

    @Override
    public String toString() {
        return "MHNotifyInfos{" +
                "id=" + id +
                ", object='" + object + '\'' +
                ", userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", subType='" + subType + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", lon='" + lon + '\'' +
                ", lat='" + lat + '\'' +
                ", typeContent='" + typeContent + '\'' +
                ", subTypeContent='" + subTypeContent + '\'' +
                ", specificContent='" + specificContent + '\'' +
                '}';
    }
}
