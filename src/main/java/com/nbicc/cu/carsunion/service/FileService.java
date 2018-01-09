package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.constant.ParameterValues;
import com.nbicc.cu.carsunion.dao.SwUpdateDao;
import com.nbicc.cu.carsunion.enumtype.ResponseType;
import com.nbicc.cu.carsunion.model.SwUpdate;
import com.nbicc.cu.carsunion.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
public class FileService {

    @Autowired
    private SwUpdateDao swUpdateDao;

    @Transactional(rollbackFor = Exception.class)
    public boolean uploadSwUpdate(MultipartFile file,String version,String fileName) throws IOException {
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        String filePath = ParameterValues.SW_UPDATE_FILE_PATH_LOCAL_TEST;
        // 解决中文问题，liunx下中文路径，图片显示问题
        fileName = CommonUtil.generateUUID32() + suffixName;

        SwUpdate swUpdate = new SwUpdate();
        swUpdate.setVersion(version);
        swUpdate.setFileName(fileName);
        swUpdate.setUploadTime(new Date());
        swUpdateDao.save(swUpdate);

        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        file.transferTo(dest);
        return true;
    }

    public SwUpdate getLatestSwUpdate(){
        return swUpdateDao.findLatestUpdate();
    }
}
