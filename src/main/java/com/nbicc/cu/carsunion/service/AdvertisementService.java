package com.nbicc.cu.carsunion.service;

import com.nbicc.cu.carsunion.dao.AdvertisementDao;
import com.nbicc.cu.carsunion.model.Advertisement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertisementService {

    @Autowired
    private AdvertisementDao advertisementDao;


    public List<Advertisement> getIndexAdvertisement() {
        return advertisementDao.findByIsShow(0);
    }


    public List<Advertisement> getAllAdvertisement() {
        return advertisementDao.findAll();
    }

    public void editAdvertisement(String id, int location, int photoType, String photoContent, String photo, int isShow) {
        Advertisement ad = advertisementDao.findOne(Long.parseLong(id));
        ad.setLocation(location);
        ad.setPhotoType(photoType);
        ad.setPhotoContent(photoContent);
        ad.setPhoto(photo);
        ad.setIsShow(isShow);
        advertisementDao.save(ad);
    }

    public Advertisement getAdvertisementById(Long id) {
        return advertisementDao.findOne(id);
    }

}
