package com.spring.oshaneat.controller;

import com.spring.oshaneat.payload.ResponseData;
import com.spring.oshaneat.service.imp.FileServiceImp;
import com.spring.oshaneat.service.imp.MenuServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController()
@RequestMapping("/menu")
@CrossOrigin("*")
public class MenuController {

    @Autowired
    MenuServiceImp menuServiceImp;

    @Autowired
    FileServiceImp fileServiceImp;

    @PostMapping()
    public ResponseEntity<?> createMenu(@RequestParam String title,
                                              @RequestParam MultipartFile file,
                                              @RequestParam boolean isFreeShip,
                                              @RequestParam String timeShip,
                                              @RequestParam Long price,
                                              @RequestParam int cateId
    ) {
        ResponseData responseData = new ResponseData();
        boolean isSuccess = menuServiceImp.insertFood(title, file, isFreeShip, timeShip, price, cateId);
        responseData.setData(isSuccess);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping("/file/{filename:.+}")
    public ResponseEntity<?> getFileMenu(@PathVariable String filename) {
        Resource resource = fileServiceImp.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping()
    public ResponseEntity<?> searchMenu(@RequestParam(required = false) String keyword) {
        ResponseData responseData = menuServiceImp.searchByKeyword(keyword);
        return new ResponseEntity<>(responseData, responseData.getHttpStatus());
    }
}
