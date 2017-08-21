package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.model.Vehicle;
import com.nbicc.cu.carsunion.service.VehicleService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    VehicleService vehicleService;

    @RequestMapping(value = "/addVehicle", method = RequestMethod.POST)
    public JSONObject addVehicle(@RequestParam(value = "id", required = false) String pid,
                                 @RequestParam(value = "path", required = false) String path,
                                 @RequestParam(value = "name") String name,
                                 @RequestParam(value = "level", required = false) Integer level,
                                 @RequestParam(value = "logo", required = false) String logo) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            boolean state = vehicleService.addVehicle(path, pid, name, level, logo);
            res.put(ParameterKeys.STATE, state ? 0 : 1);
        } catch (Exception e) {
            e.printStackTrace();
            res.put(ParameterKeys.STATE, 1);
        }
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,res);
    }

    @RequestMapping(value = "/deleteVehicles", method = RequestMethod.POST)
    public JSONObject deleteVehicles(@RequestParam(value = "id") String id,
                                     @RequestParam(value = "path", required = false) String path) {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            boolean state = vehicleService.deleteVehicles(path, id);
            res.put(ParameterKeys.STATE, state ? 0 : 1);
        } catch (Exception e) {
            e.printStackTrace();
            res.put(ParameterKeys.STATE, 1);
        }
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,res);
    }

    @RequestMapping(value = "/getVehiclesByLevel", method = RequestMethod.POST)
    public JSONObject getVehiclesByLevel(@RequestParam(value = "id", required = false) String pid,
                                         @RequestParam(value = "path", required = false) String path){
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            List<Vehicle> vlist = vehicleService.getVehiclesByLevel(path, pid);
            res.put("vehicleList", vlist);
            res.put(ParameterKeys.STATE, 0);
        }catch(Exception e) {
            e.printStackTrace();
            res.put(ParameterKeys.STATE, 1);
        }
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,res);
    }

    @RequestMapping(value = "/getFullName", method = RequestMethod.POST)
    public JSONObject getFullName(@RequestParam(value = "id") String id,
                                  @RequestParam(value = "path", required = false) String path){
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            List<String> nameList = vehicleService.getFullName(path, id);
            res.put("nameList", nameList);
            res.put(ParameterKeys.STATE, 0);
        }catch(Exception e) {
            e.printStackTrace();
            res.put(ParameterKeys.STATE, 1);
        }
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,res);
    }
}
