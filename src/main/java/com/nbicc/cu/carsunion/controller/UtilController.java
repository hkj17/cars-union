package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.util.Auth;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.nbicc.cu.carsunion.constant.ParameterValues.accessKey;
import static com.nbicc.cu.carsunion.constant.ParameterValues.secretKey;

/**
 * Created by bigmao on 2017/8/21.
 */
@RestController
@RequestMapping("/util")
public class UtilController {

    //给js提供七牛的uptoken，option为1表示私密上传。
    @RequestMapping(value = "getUptoken",method = RequestMethod.GET)
    public JSONObject getUptoken(@RequestParam(value = "option",required = false)String option){
        String bucket = "photo";
        if(option!=null && option.equals("1")){
            bucket = "private";
        }
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uptoken",upToken);
        return jsonObject;
    }

//    @RequestMapping(value = "uploadPhoto",method = RequestMethod.GET)
//    public JSONObject uploadPhoto(@RequestParam(value = "option")int option){
//        Configuration cfg = new Configuration(Zone.zone0());
//        UploadManager uploadManager = new UploadManager(cfg);
//        String bucket;
//        if(option == 0) {
//            bucket = "photo";
//        }else{
//            bucket = "private";
//        }
//        String localFilePath = "/Users/bigmao/Downloads/bd_logo1.jpg";
//        String key = "test_bd_photo.jpg";
//        Auth auth = Auth.create(accessKey,secretKey);
//        String upToken = auth.uploadToken(bucket);
//        JSONObject json = new JSONObject();
//
//        try{
//            Response response = uploadManager.put(localFilePath,key,upToken);
//            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(),DefaultPutRet.class);
//            if(option == 0) {
//                json.put("url", QiniuUtil.photoUrlForPublic(putRet.key));
//            }else{
//                json.put("private url",QiniuUtil.photoUrlForPrivate(putRet.key));
//            }
//        }catch (QiniuException ex){
//            Response r = ex.response;
//            System.err.println(r.toString());
//            try{
//                System.err.println(r.bodyString());
//            }catch (QiniuException ex2){
//
//            }
//        }
//        return json;
//    }
}
