package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.Authority;
import com.nbicc.cu.carsunion.constant.AuthorityType;
import com.nbicc.cu.carsunion.enumtype.ResponseType;
import com.nbicc.cu.carsunion.model.Present;
import com.nbicc.cu.carsunion.model.HostHolder;
import com.nbicc.cu.carsunion.model.UserPresentHistory;
import com.nbicc.cu.carsunion.service.PresentService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/present")
@Authority
public class PresentController {
    @Autowired
    PresentService presentService;

    @Autowired
    private HostHolder hostHolder;

    @Authority(value = AuthorityType.AdminValidate)
    @PostMapping(value = "addPresent")
    public JSONObject addPresent(@RequestParam(value = "name") String name,
                                 @RequestParam(value = "photo") String photo,
                                 @RequestParam(value = "value") int creditValue,
                                 @RequestParam(value = "quantity") int quantity,
                                 @RequestParam(value = "onSale") boolean onSale){
        boolean state = presentService.addPresent(name,photo,creditValue,quantity,onSale);
        return CommonUtil.response(state);
    }

    @Authority(value = AuthorityType.AdminValidate)
    @PostMapping(value = "deletePresent")
    public JSONObject deletePresent(@RequestParam(value = "id") long id){
        boolean state = presentService.deletePresent(id);
        return CommonUtil.response(state);
    }

    @Authority(value = AuthorityType.AdminValidate)
    @PostMapping(value = "editPresent")
    public JSONObject editPresent(@RequestParam(value = "id") long id,
                                  @RequestParam(value = "name") String name,
                                  @RequestParam(value = "photo") String photo,
                                  @RequestParam(value = "value") int creditValue){
        boolean state = presentService.editPresent(id,name,photo,creditValue);
        return CommonUtil.response(state);
    }

    @Authority(value = AuthorityType.AdminValidate)
    @PostMapping(value = "editPresentOnSale")
    public JSONObject editPresentOnSale(@RequestParam(value = "id") long id,
                                        @RequestParam(value = "onSale") boolean onSale){
        boolean state = presentService.editPresentOnSale(id,onSale);
        return CommonUtil.response(state);
    }

    @Authority(value = AuthorityType.AdminValidate)
    @PostMapping(value = "getAllPresents")
    public JSONObject getAllPresents(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        Page<Present> presentList = presentService.getAllPresentList(pageNum-1,pageSize);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",presentList);
    }

    @Authority(value = AuthorityType.UserValidate)
    @PostMapping(value = "getOnSalePresents")
    public JSONObject getOnSalePresents(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        Page<Present> presentList = presentService.getOnSalePresentList(pageNum-1,pageSize);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",presentList);
    }

    @Authority(value = AuthorityType.AdminValidate)
    @PostMapping(value = "getPresentById")
    public JSONObject getPresentById(@RequestParam(value = "id") long id) {
        Present present = presentService.getPresentById(id);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "返回成功", present);
    }

    @PostMapping("/exchange")
    @Authority(AuthorityType.UserValidate)
    public JSONObject exchange(@RequestParam("presentId")long presentId,
                               @RequestParam(value = "num",defaultValue = "1")int num,
                               @RequestParam("address")String address){
        boolean result = presentService.exchange(hostHolder.getAdmin().getId(),presentId,num,address);
        if(result){
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"积分兑换成功",null);
        }else{
            return CommonUtil.response(ResponseType.REQUEST_FAIL,"积分兑换失败",null);
        }
    }

    @GetMapping("/history")
    @Authority(AuthorityType.UserValidate)
    public JSONObject history(@RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                              @RequestParam(value = "pageSize",defaultValue = "6")int pageSize){
        Page<UserPresentHistory> result = presentService.history(hostHolder.getAdmin().getId(),pageNum-1,pageSize);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"查询成功",result);
    }


    @GetMapping("/historyByAdmin")
    @Authority(AuthorityType.AdminValidate)
    public JSONObject historyByAdmin(@RequestParam(value = "presentId",defaultValue = "-1")long presentId,
                                     @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                                     @RequestParam(value = "pageSize",defaultValue = "6")int pageSize){
        Page<UserPresentHistory> result = presentService.historyByAdmin(presentId,pageNum-1,pageSize);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"查询成功",result);
    }

    @PostMapping("/sendMark")
    @Authority(AuthorityType.AdminValidate)
    public JSONObject sendMark(@RequestParam(value = "id")long id){
        boolean result = presentService.sendMark(id);
        if(result){
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"发货标记成功",null);
        }else{
            return CommonUtil.response(ResponseType.REQUEST_FAIL,"发货标记失败",null);
        }
    }
}
