package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.dao.AdvertisementDao;
import com.nbicc.cu.carsunion.model.Advertisement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertisementService {

    @Autowired
    private AdvertisementDao advertisementDao;


    /**
     * app端可见的广告列表
     *
     * @return
     */
    public List<Advertisement> getIndexAdvertisement() {
        return advertisementDao.findByIsShow(0);
    }


    /**
     * 管理后台，显示所有广告信息，可按位置查询，支持分页
     *
     * @param location
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Page<Advertisement> getAllAdvertisement(int location, int pageNum, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);
        return advertisementDao.findByLocation(location, pageable);
    }

    /**
     * 编辑广告
     *
     * @param id
     * @param location
     * @param photoType
     * @param photoContent
     * @param photo
     * @param isShow
     */
    public void editAdvertisement(long id, int location, int photoType, String photoContent, String photo, int isShow) {
        Advertisement ad = advertisementDao.findOne(id);
        ad.setLocation(location);
        ad.setPhotoType(photoType);
        ad.setPhotoContent(photoContent);
        ad.setPhoto(photo);
        ad.setIsShow(isShow);
        advertisementDao.save(ad);
    }

    /**
     * 根据广告id获取广告信息
     *
     * @param id
     * @return
     */
    public Advertisement getAdvertisementById(Long id) {
        return advertisementDao.findOne(id);
    }

    /**
     * 新增广告
     *
     * @param location
     * @param photoType
     * @param photoContent
     * @param photo
     * @param isShow
     * @return
     */
    public Long addAdvertisement(int location, int photoType, String photoContent, String photo, int isShow) {
        Advertisement ad = new Advertisement();
        ad.setLocation(location);
        ad.setPhotoType(photoType);
        ad.setPhotoContent(photoContent);
        ad.setPhoto(photo);
        ad.setIsShow(isShow);
        advertisementDao.save(ad);
        return ad.getId();
    }

    /**
     * 删除广告
     *
     * @param id
     */
    public void deleteAdvertisement(long id) {
        Advertisement ad = advertisementDao.findOne(id);
        if (ad != null) {
            advertisementDao.delete(ad);
        }
    }
}
