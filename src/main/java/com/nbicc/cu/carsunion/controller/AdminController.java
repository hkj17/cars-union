package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.Authority;
import com.nbicc.cu.carsunion.constant.AuthorityType;
import com.nbicc.cu.carsunion.enumtype.ResponseType;
import com.nbicc.cu.carsunion.model.Merchant;
import com.nbicc.cu.carsunion.service.MerchantService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.QiniuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Authority
public class AdminController {
    @Autowired
    MerchantService merchantService;

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
    public JSONObject passRegistration(@RequestParam(value = "contact") String contact) {
        boolean state = merchantService.passRegistration(contact);
        if (state) {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "操作成功",null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "操作失败",null);
        }
    }

    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "/failRegistration", method = RequestMethod.POST)
    public JSONObject failRegistration(@RequestParam(value = "contact") String contact) {
        boolean state = merchantService.failRegistration(contact);
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
}
