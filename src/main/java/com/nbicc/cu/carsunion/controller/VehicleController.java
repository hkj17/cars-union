package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.ParameterKeys;
import com.nbicc.cu.carsunion.model.Vehicle;
import com.nbicc.cu.carsunion.model.VehicleTreeModel;
import com.nbicc.cu.carsunion.service.VehicleService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        Vehicle newVehicle = vehicleService.addVehicle(path, pid, name, level, logo);
        if(newVehicle != null){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,newVehicle);
        }else{
            return CommonUtil.response(ParameterKeys.REQUEST_FAIL,"error");
        }
    }

    @RequestMapping(value = "/deleteVehicles", method = RequestMethod.POST)
    public JSONObject deleteVehicles(@RequestParam(value = "id") String id,
                                     @RequestParam(value = "path", required = false) String path) {
        boolean state = vehicleService.deleteVehicles(path, id);
        if(state){
            return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,"ok");
        }else{
            return CommonUtil.response(ParameterKeys.REQUEST_FAIL,"error");
        }
    }

    @RequestMapping(value = "/getVehiclesByLevel", method = RequestMethod.POST)
    public JSONObject getVehiclesByLevel(@RequestParam(value = "id", required = false) String pid,
                                         @RequestParam(value = "path", required = false) String path){
        List<Vehicle> vehicleList = vehicleService.getVehiclesByLevel(path, pid);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,vehicleList);
    }

    @RequestMapping(value = "/getFullName", method = RequestMethod.POST)
    public JSONObject getFullName(@RequestParam(value = "id") String id,
                                  @RequestParam(value = "path", required = false) String path){
        List<String> nameList = vehicleService.getFullName(path, id);
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,nameList);
    }


    @RequestMapping(value = "getVehicleTrees", method = {RequestMethod.GET,RequestMethod.POST})
    public JSONObject getVehicleTrees(){
        List<VehicleTreeModel> lists = vehicleService.getVehicleTrees();
        return CommonUtil.response(ParameterKeys.REQUEST_SUCCESS,lists);
    }

}
