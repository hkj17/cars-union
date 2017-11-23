package com.nbicc.cu.carsunion.service;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.util.MHUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MHService {

    public List<JSONObject> getVBrand(){
        String vBrandStr = MHUtil.getMHVBrandList();
        Object[] vBrand = JSONObject.parseObject(vBrandStr).getJSONArray("data").toArray();
        List<JSONObject> result = new ArrayList<>();
        for(Object object : vBrand){
            JSONObject tem = JSONObject.parseObject(object.toString());
            tem.remove("attributeNames");
            if("".equals(tem.getString("peccancyPrice"))){
                result.add(tem);
            }
        }
        return result;
    }

    public List<JSONObject> getMHVStyle(String brandId) {
        String vStyleStr = MHUtil.getMHVStyleList(brandId);
        Object[] vStyle = JSONObject.parseObject(vStyleStr).getJSONArray("data").toArray();
        List<JSONObject> result = new ArrayList<>();
        for(Object object : vStyle){
            JSONObject tem = JSONObject.parseObject(object.toString());
            tem.remove("attributeNames");
            result.add(tem);
        }
        return result;
    }


    public List<JSONObject> getMHVModelList(String styleId) {
        String vModelStr = MHUtil.getMHVModelList(styleId);
        Object[] vModel = JSONObject.parseObject(vModelStr).getJSONArray("data").toArray();
        List<JSONObject> result = new ArrayList<>();
        for(Object object : vModel){
            JSONObject tem = JSONObject.parseObject(object.toString());
            tem.remove("attributeNames");
            result.add(tem);
        }
        return result;
    }
}
