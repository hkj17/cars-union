package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.Authority;
import com.nbicc.cu.carsunion.constant.AuthorityType;
import com.nbicc.cu.carsunion.enumtype.ResponseType;
import com.nbicc.cu.carsunion.model.Merchant;
import com.nbicc.cu.carsunion.model.Order;
import com.nbicc.cu.carsunion.model.UserFeedback;
import com.nbicc.cu.carsunion.model.UserQueryProduct;
import com.nbicc.cu.carsunion.service.AdminService;
import com.nbicc.cu.carsunion.service.MerchantService;
import com.nbicc.cu.carsunion.service.OrderService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.QiniuUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Authority
public class AdminController {

    private static Logger logger = LogManager.getLogger(AdminController.class);

    @Autowired
    MerchantService merchantService;

    @Autowired
    OrderService orderService;

    @Autowired
    AdminService adminService;

    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "/getRegInProcessList", method = RequestMethod.POST)
    public JSONObject getRegInProcessList(@RequestParam(value = "status") int status,
                                          @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page<Merchant> merchantPage = merchantService.getRegInProcessList(status, pageNum, pageSize);
        List<Merchant> merchantList = merchantPage.getContent();
        for (Merchant m : merchantList) {
            if(!CommonUtil.isNullOrEmpty(m.getIdcardFront())) {
                m.setIdcardFront(QiniuUtil.photoUrlForPrivate(m.getIdcardFront()));
            }
            if(!CommonUtil.isNullOrEmpty(m.getIdcardBack())) {
                m.setIdcardBack(QiniuUtil.photoUrlForPrivate(m.getIdcardBack()));
            }
            if(!CommonUtil.isNullOrEmpty(m.getLicensePath())) {
                m.setLicensePath(QiniuUtil.photoUrlForPrivate(m.getLicensePath()));
            }
            if(!CommonUtil.isNullOrEmpty(m.getLogo())){
                m.setLogo(QiniuUtil.photoUrlForPublic(m.getLogo()));
            }
        }
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",merchantPage);
    }

    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "/passRegistration", method = RequestMethod.POST)
    public JSONObject passRegistration(@RequestParam(value = "phone") String phone) {
        boolean state = merchantService.passRegistration(phone);
        if (state) {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "操作失败",null);
        }
    }

    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "/failRegistration", method = RequestMethod.POST)
    public JSONObject failRegistration(@RequestParam(value = "phone") String phone) {
        boolean state = merchantService.failRegistration(phone);
        if (state) {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "操作失败",null);
        }
    }

    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "/modifyMerchantInfo", method = RequestMethod.POST)
    public JSONObject modifyMerchantInfo(@RequestParam(value = "id") String id,
                                         @RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "address", required = false) String address,
                                         @RequestParam(value = "region", required = false) String region,
                                         @RequestParam(value = "contact", required = false) String contact,
                                         @RequestParam(value = "longitude", required = false) String longitude,
                                         @RequestParam(value = "latitude", required = false) String latitude,
                                         @RequestParam(value = "logo", required = false) String logo) {
        boolean state = merchantService.modifyMerchantInfo(id, name, address, region, contact, longitude, latitude,logo);
        if (state) {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "操作失败",null);
        }
    }

    //带分页,所有最新订单列表（管理员查看）
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "getOrderList", method = RequestMethod.POST)
    public JSONObject getOrderListWithPage(@RequestParam(value = "start", defaultValue = "2017-01-01 00:00:00") String startDate,
                                           @RequestParam(value = "end", defaultValue = "2050-01-01 00:00:00") String endDate,
                                           @RequestParam(value = "status",defaultValue = "-1") int status,
                                           @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page<Order> orders = orderService.getOrderListByTimeWithPage(startDate, endDate,status, pageNum - 1, pageSize);
        if(CommonUtil.isNullOrEmpty(orders)){
            return CommonUtil.response(ResponseType.REQUEST_FAIL,"日期格式错误，请输入 yyyy-MM-dd 格式",null);
        }else {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "返回成功", orders);
        }
    }

    @Authority(value = AuthorityType.AdminValidate)
    @PostMapping(value = "/getAllQueriedProducts")
    public JSONObject getAllQueriedProducts(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        Page<UserQueryProduct> userQueryProducts = adminService.getAllQueriedProducts(pageNum-1,pageSize);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",userQueryProducts);
    }

    @Authority(value = AuthorityType.AdminValidate)
    @PostMapping(value = "/getAllUserFeedback")
    public JSONObject getAllUserFeedback(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        Page<UserFeedback> feedbacks = adminService.getAllUserFeedback(pageNum-1,pageSize);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",feedbacks);
    }
}
