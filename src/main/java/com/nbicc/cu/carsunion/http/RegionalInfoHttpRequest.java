package com.nbicc.cu.carsunion.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.ParameterValues;
import com.nbicc.cu.carsunion.http.data.RegionalInfo;
import com.nbicc.cu.carsunion.util.CommonUtil;

import javax.swing.plaf.synth.Region;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RegionalInfoHttpRequest extends HttpRequest {
    public RegionalInfoHttpRequest(){
        super(ParameterValues.URL_REGIONAL);
        super.setParamater("subdistrict", 3);
        super.setParamater("key",ParameterValues.REGIONAL_KEY);
    }

    public List<RegionalInfo> getDistricts(String province, String city, String district){
        super.getResponse();
        JSONObject json = JSONObject.parseObject(response);
        JSONArray jArray = json.getJSONArray("districts").getJSONObject(0).getJSONArray("districts");
        if(CommonUtil.isNullOrEmpty(province)){
            return getRegionalInfoList(jArray);
        }else{
            jArray = getNextLevelArray(jArray, province);
        }
        if(jArray == null){
            return new ArrayList<RegionalInfo>();
        }

        if(CommonUtil.isNullOrEmpty(city)){
            return getRegionalInfoList(jArray);
        }else{
            jArray = getNextLevelArray(jArray, city);
        }
        if(jArray == null){
            return new ArrayList<RegionalInfo>();
        }

        if(CommonUtil.isNullOrEmpty(district)){
            return getRegionalInfoList(jArray);
        }else{
            List<RegionalInfo> districtList = new ArrayList<RegionalInfo>();
            for(int i=0;i<jArray.size();i++){
                JSONObject jObject = jArray.getJSONObject(i);
                if(district.equals(jObject.get("name"))){
                    RegionalInfo info = new RegionalInfo();
                    info.setName(jObject.getString("name"));
                    String[] locationInfo = jObject.getString("center").split(",");
                    info.setLongitude(locationInfo[0]);
                    info.setLatitude(locationInfo[1]);
                    districtList.add(info);
                    break;
                }

            }
            return districtList;
        }
    }

    private JSONArray getNextLevelArray(JSONArray jArray, String key){
        for(int i=0;i<jArray.size();i++) {
            String name = jArray.getJSONObject(i).getString("name");
            if (key.equals(name)) {
                return jArray.getJSONObject(i).getJSONArray("districts");
            }
        }
        return null;
    }

    private List<RegionalInfo> getRegionalInfoList(JSONArray jArray){
        List<RegionalInfo> districtList = new ArrayList<RegionalInfo>();
        if(jArray == null){
            return districtList;
        }
        for(int i=0;i<jArray.size();i++){
            JSONObject jObject = jArray.getJSONObject(i);
            RegionalInfo info = new RegionalInfo();
            info.setName(jObject.getString("name"));
            String[] locationInfo = jObject.getString("center").split(",");
            info.setLongitude(locationInfo[0]);
            info.setLatitude(locationInfo[1]);
            districtList.add(info);
        }
        return districtList;
    }
}
