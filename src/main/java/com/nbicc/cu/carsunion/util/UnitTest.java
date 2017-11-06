package com.nbicc.cu.carsunion.util;

import com.nbicc.cu.carsunion.enumtype.OrderStatus;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class UnitTest {

    public static void main(String[] args){
        BigDecimal decimal = new BigDecimal("0.01");
        DecimalFormat df=new DecimalFormat("0.00");
        System.out.println(df.format(decimal));
    }
}
