package com.spring.oshaneat.controller;

import com.spring.oshaneat.payload.ResponseData;
import com.spring.oshaneat.payload.request.FilterRequest;
import com.spring.oshaneat.service.imp.CategoryServiceImp;
import com.spring.oshaneat.service.imp.FileServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
@CrossOrigin("*")
public class CategoryController {

    @Autowired
    CategoryServiceImp categoryServiceImp;

    @Autowired
    FileServiceImp fileServiceImp;

    @GetMapping()
    public ResponseEntity<?> getHomeCategories() {
        ResponseData responseData = new ResponseData();
        responseData.setData(categoryServiceImp.getHomeCategories());
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getAllFoodInCategory(@PathVariable int categoryId) {
        ResponseData responseData = categoryServiceImp.getAllFoodInCategory(categoryId);
        return new ResponseEntity<>(responseData, responseData.getHttpStatus());
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterFood(@RequestParam Integer categoryId,
                                        @RequestParam(required = false) String minPrice,
                                        @RequestParam(required = false) String maxPrice,
                                        @RequestParam(required = false, defaultValue = "asc") String sortBy) {
        long min;
        long max;
        try {
            min = Long.parseLong(minPrice);
        } catch (NumberFormatException e) {
            min = 0L;
        }
        try {
            max = Long.parseLong(maxPrice);
        } catch (NumberFormatException e) {
            max = Integer.MAX_VALUE;
        }
        ResponseData responseData = categoryServiceImp.filterFood(categoryId, min, max, sortBy);
        return new ResponseEntity<>(responseData, responseData.getHttpStatus());
    }

    private Long parseLongOrNull(String str) {
        if (str == null || "null".equals(str)) {
            return (long) Integer.MAX_VALUE;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCategories() {
        ResponseData responseData = categoryServiceImp.getAll();
        return new ResponseEntity<>(responseData, responseData.getHttpStatus());
    }

    @GetMapping("/file/{filename:.+}")
    public ResponseEntity<?> getFileCategory(@PathVariable String filename) {
        Resource resource = fileServiceImp.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
