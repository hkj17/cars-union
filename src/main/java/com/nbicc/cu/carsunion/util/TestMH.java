package com.nbicc.cu.carsunion.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestMH {
    public static void main(String[] args){
        //System.out.println(MHUtil.getMHVBrandList());
        //System.out.println(MHUtil.getMHVStyleList("130"));
        //System.out.println(MHUtil.getMHVModelList("680"));
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //System.out.println(MHUtil.addMHVehicle("粤B12345","122111112222222","130","189","1318","1331054","13411112222","868120157321461",now));
        //System.out.println(MHUtil.updateMHVehicle("133","粤B67890","122111112222222","130","189","1318","13411112222",now));
        //System.out.println(MHUtil.getMHVehicleDetails("133"));
        //System.out.println(MHUtil.getMHVehicleStatus("3080"));
        //System.out.println(MHUtil.getMHVehiclePosition("3080"));
        System.out.println(MHUtil.getMHAlertTypeList());
    }
}
