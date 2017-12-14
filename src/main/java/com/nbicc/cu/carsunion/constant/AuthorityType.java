package com.nbicc.cu.carsunion.constant;

/**
 * Created by bigmao on 2017/9/15.
 */
public enum AuthorityType {
    // 用户权限
    UserValidate,

    //商家权限
    MerchantValidate,

    //后台管理员权限
    AdminValidate,

    // 不验证，默认
    NoValidate,

    // 不验证权限,类似用户权限
    LoginAthority
}
