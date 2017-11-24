package com.nbicc.cu.carsunion.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.ParameterValues;
import com.nbicc.cu.carsunion.model.RegionalInfo;
import com.nbicc.cu.carsunion.util.CommonUtil;
import com.nbicc.cu.carsunion.util.PinyinUtil;

import java.util.*;

public class RegionalInfoHttpRequest extends HttpRequest {
    public RegionalInfoHttpRequest(){
        super(ParameterValues.URL_REGIONAL);
        paramMap.put("subdistrict",3);
        paramMap.put("key",ParameterValues.REGIONAL_KEY);
    }

    public List<RegionalInfo> getDistricts(String province, String city, String district){
        super.getCachedResponseGET();
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

    public List<RegionalInfo> getAllCities(){
        super.getCachedResponseGET();
        List<RegionalInfo> regionalInfos = new ArrayList<>();
        JSONObject json = JSONObject.parseObject(response);
        JSONArray jProvinceArray = json.getJSONArray("districts").getJSONObject(0).getJSONArray("districts");
        for(int i=0;i<jProvinceArray.size();i++){
            JSONObject jObject = jProvinceArray.getJSONObject(i);
            JSONArray jCityArray = jObject.getJSONArray("districts");
            List<RegionalInfo> tempList = getRegionalInfoList(jCityArray);
            regionalInfos.addAll(tempList);
        }
        Comparator<RegionalInfo> comparator = new Comparator<RegionalInfo>() {
            @Override
            public int compare(RegionalInfo o1, RegionalInfo o2) {
                return (int) o1.getInitial() - o2.getInitial();
            }
        };
        Collections.sort(regionalInfos,comparator);
        return regionalInfos;
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
            String name = jObject.getString("name");
            info.setName(name);
            String pinyin = PinyinUtil.getPinyin(name);
            info.setPinyin(pinyin);
            if(!CommonUtil.isNullOrEmpty(pinyin)){
                info.setInitial(pinyin.charAt(0));
            }
            String[] locationInfo = jObject.getString("center").split(",");
            info.setLongitude(locationInfo[0]);
            info.setLatitude(locationInfo[1]);
            districtList.add(info);
        }
        return districtList;
    }
}
