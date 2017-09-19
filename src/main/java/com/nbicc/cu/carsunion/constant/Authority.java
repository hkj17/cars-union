package com.nbicc.cu.carsunion.constant;

import java.lang.annotation.*;

/**
 * Created by bigmao on 2017/9/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Authority {
    AuthorityType value() default AuthorityType.NoValidate;
}
