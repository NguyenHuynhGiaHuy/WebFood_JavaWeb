package com.spring.oshaneat.service.imp;

import com.spring.oshaneat.payload.ResponseData;
import org.springframework.web.multipart.MultipartFile;

public interface MenuServiceImp {
    boolean insertFood(String title, MultipartFile file, boolean isFreeShip, String timeShip, Long price, int cateId);
    ResponseData searchByKeyword(String keyword);
}
