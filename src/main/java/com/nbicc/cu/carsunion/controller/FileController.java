package com.nbicc.cu.carsunion.controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.cu.carsunion.constant.Authority;
import com.nbicc.cu.carsunion.constant.AuthorityType;
import com.nbicc.cu.carsunion.enumtype.ResponseType;
import com.nbicc.cu.carsunion.model.SwUpdate;
import com.nbicc.cu.carsunion.service.FileService;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
@Authority
public class FileController {

    @Autowired
    private FileService fileService;

    @Authority(value = AuthorityType.AdminValidate)
    @PostMapping(value = "/upload")
    public JSONObject upload(@RequestParam("file") MultipartFile file,
                              @RequestParam("version") String version) {
        if (file.isEmpty()) {
            return CommonUtil.response(ResponseType.REQUEST_FAIL,"文件为空",null);
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        try {
            boolean state = fileService.uploadSwUpdate(file,version,fileName);
            return CommonUtil.response(state);
        } catch (IOException e) {
            e.printStackTrace();
            return CommonUtil.response(ResponseType.REQUEST_FAIL,"上传失败",null);
        }
    }

    @Authority(value = AuthorityType.UserValidate)
    @GetMapping(value = "/update")
    public JSONObject getUpdate(){
        SwUpdate swUpdate = fileService.getLatestSwUpdate();
        return CommonUtil.response(ResponseType.REQUEST_SUCCESS,"返回成功",swUpdate);
    }

}
