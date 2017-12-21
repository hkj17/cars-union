package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.Authority;
import com.nbicc.cu.carsunion.constant.AuthorityType;
import com.nbicc.cu.carsunion.enumtype.ResponseType;
import com.nbicc.cu.carsunion.model.CreditHistory;
import com.nbicc.cu.carsunion.model.HostHolder;
import com.nbicc.cu.carsunion.model.UserSignHistory;
import com.nbicc.cu.carsunion.service.CreditService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/credit")
@Authority(value = AuthorityType.UserValidate)
public class CreditController {
    @Autowired
    private CreditService creditService;
    @Autowired
    private HostHolder hostHolder;

    /**
     * 用户签到接口
     */
    @GetMapping("/sign")
    public JSONObject UserSign(){
        int result = creditService.userSign(hostHolder.getAdmin().getId());
        if(result == 0){
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"签到成功",null);
        }else{
            return CommonUtil.response(ResponseType.REQUEST_FAIL,"今日已签到",null);
        }
    }

    /**
     * 用户签到记录查询接口
     */
    @GetMapping("/signHistory")
    public JSONObject signHistory(@RequestParam("start") String start,
                                  @RequestParam("end")String end,
                                  @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                                  @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        Page<UserSignHistory> result = null;
        try {
            result = creditService.signHistory(hostHolder.getAdmin().getId(),start,end,pageNum-1,pageSize);
        } catch (ParseException e) {
            return CommonUtil.response(ResponseType.REQUEST_FAIL,"日期格式错误，请输入2017-01-01格式",null);
        }
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"查询成功",result);
    }

    /**
     * 用户总积分和会员等级查询查询接口
     */
    @GetMapping("/overview")
    public JSONObject overview(){
        JSONObject result = creditService.overview(hostHolder.getAdmin().getId());
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"查询成功",result);
    }

    /**
     * 用户消费积分历史记录查询接口
     */
    @PostMapping("/shoppingCreditHistory")
    public JSONObject getCreditHistory(@RequestParam(value = "source",defaultValue = "-1") int source,
                                       @RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                                       @RequestParam(value = "pageSize",defaultValue = "6")int pageSize){
        String userId = hostHolder.getAdmin().getId();
        Page<CreditHistory> creditHistoryList = creditService.getUserCreditHistory(userId,source,pageNum-1,pageSize);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",creditHistoryList);
    }



}
