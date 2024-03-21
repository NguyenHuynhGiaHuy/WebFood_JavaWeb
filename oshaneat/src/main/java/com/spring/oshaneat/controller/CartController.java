package com.spring.oshaneat.controller;

import com.spring.oshaneat.payload.ResponseData;
import com.spring.oshaneat.payload.request.CartRequest;
import com.spring.oshaneat.service.imp.CartServiceImp;
import com.spring.oshaneat.service.imp.FileServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    CartServiceImp cartServiceImp;

    @Autowired
    FileServiceImp fileServiceImp;

    @PostMapping()
    public ResponseEntity<?> addFoodToCart(@AuthenticationPrincipal UserDetails user, @RequestBody @Valid CartRequest cartRequest) {
        ResponseData responseData = cartServiceImp.addCart(user, cartRequest);
        return new ResponseEntity<>(responseData, responseData.getHttpStatus());
    }

    @GetMapping()
    public ResponseEntity<?> getUserCarts(@AuthenticationPrincipal UserDetails user) {
        ResponseData responseData = cartServiceImp.getUserCart(user);
        return new ResponseEntity<>(responseData, responseData.getHttpStatus());
    }

    @DeleteMapping("/{foodId}")
    public ResponseEntity<?> removeFoodInCart(@AuthenticationPrincipal UserDetails user, @PathVariable int foodId) {
        ResponseData responseData = cartServiceImp.removeFoodInCart(user, foodId);
        return new ResponseEntity<>(responseData, responseData.getHttpStatus());
    }

    @GetMapping("/file/{filename:.+}")
    public ResponseEntity<?> getFileRestaurant(@PathVariable String filename) {
        Resource resource = fileServiceImp.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}

