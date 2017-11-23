package com.nbicc.cu.carsunion.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestMH {
    public static void main(String[] args){
        //System.out.println(MHUtil.getMHVBrandList());
        //System.out.println(MHUtil.getMHVStyleList("130"));
        //System.out.println(MHUtil.getMHVModelList("680"));
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println(MHUtil.addMHVehicle("粤B12345","122111112222222","130","189","1318","1331054","13411112222","868120157321461",now));
        //System.out.println(MHUtil.updateMHVehicle("133","粤B67890","122111112222222","130","189","1318","13411112222",now));
        //System.out.println(MHUtil.getMHVehicleDetails("133"));
        //System.out.println(MHUtil.getMHVehicleStatus("3080"));
        //System.out.println(MHUtil.getMHVehiclePosition("3080"));
        //System.out.println(MHUtil.getMHAlertTypeList());
       // System.out.println(MHUtil.getMHVehicleTripList("3080","2017-11-01T12:01:46+08:00","2017-11-22T16:55:46+08:00"));
        //System.out.println(MHUtil.controlMHVehicle("3080","lock",0));
        List<String> param = new ArrayList<>();
        param.add("autoLock");
        System.out.println(MHUtil.getMHVehicleSetting("3080",param));
    }
}
