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
    public JSONObject allAdvertisement(@RequestParam("location") int location,
                                       @RequestParam("pageNum",required = false,defaultValue = "1")int pageNum,
                                       @RequestParam(value = "pageSize",required = false,defaultValue = "10")int pageSize){
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "查询成功",advertisementService.getAllAdvertisement(location,pageNum-1,pageSize));
    }

    @Authority(value = AuthorityType.AdminValidate)
    @PostMapping("/editAdvertisement")
    public JSONObject editAdvertisement(@RequestParam("id")long id,
                                        @RequestParam("location")int location,
                                        @RequestParam("photoType")int photoType,
                                        @RequestParam("photoContent")String photoContent,
                                        @RequestParam("photo")String photo,
                                        @RequestParam("isShow")int isShow){
        advertisementService.editAdvertisement(id,location,photoType,photoContent,photo,isShow);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "编辑成功",advertisementService.getAdvertisementById(id));
    }

    @Authority(value = AuthorityType.AdminValidate)
    @PostMapping("/addAdvertisement")
    public JSONObject addAdvertisement(@RequestParam("location")int location,
                                        @RequestParam("photoType")int photoType,
                                        @RequestParam("photoContent")String photoContent,
                                        @RequestParam("photo")String photo,
                                        @RequestParam("isShow")int isShow){
        Long id = advertisementService.addAdvertisement(location,photoType,photoContent,photo,isShow);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "添加成功",advertisementService.getAdvertisementById(id));
    }

    @Authority(value = AuthorityType.AdminValidate)
    @PostMapping("/deleteAdvertisement")
    public JSONObject deleteAdvertisement(@RequestParam("id")long id){
        advertisementService.deleteAdvertisement(id);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "删除成功",null);
    }

}
