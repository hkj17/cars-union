package com.nbicc.cu.carsunion.util;

import java.util.Calendar;
import java.util.Date;

public class UnitTest {

    public static void main(String[] args){
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        System.out.println("calendar: " + calendar.getTime());
        System.out.println("date: " + date);
        System.out.println("availableProcessors: " + Runtime. getRuntime().availableProcessors());
    }
}
