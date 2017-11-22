package com.nbicc.cu.carsunion.controller;


import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.Authority;
import com.nbicc.cu.carsunion.constant.AuthorityType;
import com.nbicc.cu.carsunion.enumtype.ResponseType;
import com.nbicc.cu.carsunion.service.AdvertisementService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/advertise")
@Authority
public class AdvertisementController {

    @Autowired
    private AdvertisementService advertisementService;

    @GetMapping("/all")
    public JSONObject indexAdvertisement(){
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "查询成功",advertisementService.getIndexAdvertisement());
    }

    @GetMapping("/getAdvertisementById")
    public JSONObject getAdvertisementById(@RequestParam("id")Long id){
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "查询成功",advertisementService.getAdvertisementById(id));
    }

    @Authority(value = AuthorityType.AdminValidate)
    @GetMapping("/admin")
    public JSONObject allAdvertisement(){
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "查询成功",advertisementService.getAllAdvertisement());
    }

    @Authority(value = AuthorityType.AdminValidate)
    @PostMapping("/editAdvertisement")
    public JSONObject editAdvertisement(@RequestParam("id")String id,
                                        @RequestParam("location")int location,
                                        @RequestParam("photoType")int photoType,
                                        @RequestParam("photoContent")String photoContent,
                                        @RequestParam("photo")String photo,
                                        @RequestParam("isShow")int isShow){
        advertisementService.editAdvertisement(id,location,photoType,photoContent,photo,isShow);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "编辑成功",advertisementService.getAdvertisementById(Long.parseLong(id)));
    }

}
