package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.Authority;
import com.nbicc.cu.carsunion.constant.AuthorityType;
import com.nbicc.cu.carsunion.enumtype.ResponseType;
import com.nbicc.cu.carsunion.model.Vehicle;
import com.nbicc.cu.carsunion.model.VehicleProductRelationship;
import com.nbicc.cu.carsunion.model.VehicleTreeModel;
import com.nbicc.cu.carsunion.service.ProductService;
import com.nbicc.cu.carsunion.service.VehicleService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicle")
@Authority
public class VehicleController {

    @Autowired
    VehicleService vehicleService;
    @Autowired
    ProductService productService;

    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "/addVehicle", method = RequestMethod.POST)
    public JSONObject addVehicle(@RequestParam(value = "id", required = false) String pid,
                                 @RequestParam(value = "path", required = false) String path,
                                 @RequestParam(value = "name") String name,
                                 @RequestParam(value = "level", required = false) Integer level,
                                 @RequestParam(value = "logo", required = false) String logo) {
        Vehicle newVehicle = vehicleService.addVehicle(path, pid, name, level, logo);
        if (newVehicle != null) {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"操作成功",newVehicle);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "操作失败",null);
        }
    }

    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "/deleteVehicles", method = RequestMethod.POST)
    public JSONObject deleteVehicles(@RequestParam(value = "id") String id,
                                     @RequestParam(value = "path", required = false) String path) {
        boolean state = vehicleService.deleteVehicles(path, id);
        if (state) {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"操作成功",null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "操作失败",null);
        }
    }

    @RequestMapping(value = "/getVehiclesByLevel", method = RequestMethod.POST)
    public JSONObject getVehiclesByLevel(@RequestParam(value = "id", required = false) String pid,
                                         @RequestParam(value = "path", required = false) String path) {
        List<Vehicle> vehicleList = vehicleService.getVehiclesByLevel(path, pid);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "返回成功", vehicleList);
    }

    @RequestMapping(value = "/getFullName", method = RequestMethod.POST)
    public JSONObject getFullName(@RequestParam(value = "id") String id,
                                  @RequestParam(value = "path", required = false) String path) {
        List<String> nameList = vehicleService.getFullName(path, id);
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",nameList);
    }


    @RequestMapping(value = "getVehicleTrees", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject getVehicleTrees() {
        List<VehicleTreeModel> lists = vehicleService.getVehicleTrees();
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功", lists);
    }


    //批量添加车型适用商品
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "addProductForVehicle", method = RequestMethod.POST)
    public JSONObject addProductFroVehicle(@RequestBody JSONObject json) {
        String vehicleId = json.getString("vehicleId");
        String result;
        List<String> product = json.getObject("product", List.class);
        try {
            result = productService.addProductFroVehicle(vehicleId, product);
        } catch (RuntimeException e) {
            result = "add wrong";
        }
        if ("ok".equals(result)) {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, result, null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, result, null);
        }
    }

    //批量删除车型适用商品
    @Authority(value = AuthorityType.AdminValidate)
    @RequestMapping(value = "deleteProductForVehicle", method = RequestMethod.POST)
    public JSONObject deleteProductFroVehicle(@RequestBody JSONObject json) {
        String vehicleId = json.getString("vehicleId");
        String result;
        List<String> product = json.getObject("product", List.class);
        try {
            result = productService.deleteProductFroVehicle(vehicleId, product);
        } catch (RuntimeException e) {
            result = "delete wrong";
        }
        if ("ok".equals(result)) {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, result,null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, result,null);
        }
    }


    //查看车型适用商品
    @RequestMapping(value = "getVehicleRelationship", method = RequestMethod.POST)
    public JSONObject getVehicleRelationship(@RequestParam(value = "vehicleId") String vehicleId,
                                             @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page<VehicleProductRelationship> list = productService.getVehicleRelationshipByVehicle(vehicleId,
                pageNum - 1, pageSize);
        if (list == null) {
            return CommonUtil.response(ResponseType.REQUEST_FAIL, "没有该车型",null);
        } else {
            return CommonUtil.response(ResponseType.REQUEST_SUCCESS, "查询成功",list);
        }
    }

}
