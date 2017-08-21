package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.dao.VehicleDao;
import com.nbicc.cu.carsunion.model.Vehicle;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.PinyinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleService {
    @Autowired
    VehicleDao vehicleDao;

    public boolean addVehicle(String path, String pid, String name, Integer level, String logo) {
        String uuid = CommonUtil.generateUUID16();
        String currPath = "";
        if (!CommonUtil.isNullOrEmpty(pid)) {
            currPath = CommonUtil.isNullOrEmpty(path) ? "" : path + ",";
            currPath += pid;
        }
        int currLevel = level == null ? 0 : level + 1;
        String pinyin = null;
        if(CommonUtil.isNullOrEmpty(pid) && !CommonUtil.isNullOrEmpty(name)) {
            pinyin = PinyinUtil.getPinyin(name).substring(0,1).toUpperCase();
        }

        Vehicle v = new Vehicle();
        v.setId(uuid);
        v.setPath(currPath);
        v.setLevel(currLevel);
        v.setName(name);
        v.setLogo(logo);
        v.setPinyin(pinyin);
        vehicleDao.save(v);
        return true;
    }

    public boolean deleteVehicles(String path, String id) {
        Vehicle v = vehicleDao.findById(id);
        if(v == null) {
            return false;
        }
        String pattern = CommonUtil.isNullOrEmpty(path) ? "" : path + ",";
        pattern += id;
        List<Vehicle> vlist = vehicleDao.findByPathStartingWith(pattern);
        vehicleDao.deleteInBatch(vlist);
        vehicleDao.delete(v);
        return true;
    }

    public List<Vehicle> getVehiclesByLevel(String path, String id){
        if(CommonUtil.isNullOrEmpty(id)) {
            return vehicleDao.findRootVehicles();
        }else {
            String currPath = CommonUtil.isNullOrEmpty(path) ? "" : path + ",";
            currPath += id;
            return vehicleDao.findByPath(currPath);
        }
    }

    public List<String> getFullName(String path, String id){
        List<String> idList = new ArrayList<String>();
        if(!CommonUtil.isNullOrEmpty(path)) {
            String[] idArr = path.split(",");
            for (String currId:idArr) {
                idList.add(currId);
            }
        }
        idList.add(id);
        return vehicleDao.findVehicleFullName(idList);
    }
}
