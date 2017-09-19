package com.nbicc.cu.carsunion.model;

import org.springframework.stereotype.Component;

/**
 * Created by bigmao on 2017/9/15.
 */
@Component
public class HostHolder {
    private static ThreadLocal<Admin> admin = new ThreadLocal<>();

    public Admin getAdmin(){return admin.get();}

    public void setAdmin(Admin gywlwUser){admin.set(gywlwUser);}

    public void clear(){admin.remove();}
}
