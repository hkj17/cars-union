package com.nbicc.cu.carsunion.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
public class UserPresentHistory {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "present_id")
    private Present present;

    @Column(name = "num")
    private int num;

    @Column(name = "total_credit")
    private int totalCredit;

    @Column(name = "address")
    private String address;

    @Column(name = "send_mark")
    private boolean sendMark;

    @Column(name = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date date;

    public UserPresentHistory() {
    }

    public UserPresentHistory(User user, Present present, int num, int totalCredit, String address, boolean sendMark, Date date) {
        this.user = user;
        this.present = present;
        this.num = num;
        this.totalCredit = totalCredit;
        this.address = address;
        this.sendMark = sendMark;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Present getPresent() {
        return present;
    }

    public void setPresent(Present present) {
        this.present = present;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(int totalCredit) {
        this.totalCredit = totalCredit;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean getSendMark() {
        return sendMark;
    }

    public void setSendMark(boolean sendMark) {
        this.sendMark = sendMark;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
