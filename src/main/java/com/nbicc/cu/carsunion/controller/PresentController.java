package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.Authority;
import com.nbicc.cu.carsunion.constant.AuthorityType;
import com.nbicc.cu.carsunion.enumtype.ResponseType;
import com.nbicc.cu.carsunion.model.HostHolder;
import com.nbicc.cu.carsunion.model.UserPresentHistory;
import com.nbicc.cu.carsunion.service.PresentService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/present")
@Authority
public class PresentController {
    @Autowired
    PresentService presentService;
    @Autowired
    private HostHolder hostHolder;

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
