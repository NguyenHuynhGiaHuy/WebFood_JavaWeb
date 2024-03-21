package com.spring.oshaneat.controller;

import com.spring.oshaneat.payload.ResponseData;
import com.spring.oshaneat.service.imp.OrderServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderServiceImp orderServiceImp;

    @PostMapping()
    public ResponseEntity<?> insertOrder(@AuthenticationPrincipal UserDetails user) {
        ResponseData responseData = orderServiceImp.insertOrder(user);
        return new ResponseEntity<>(responseData, responseData.getHttpStatus());
    }

    @GetMapping()
    public ResponseEntity<?> getUserOrders(@AuthenticationPrincipal UserDetails user) {
        ResponseData responseData = orderServiceImp.getUserOrders(user);
        return new ResponseEntity<>(responseData, responseData.getHttpStatus());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getUserOrders(@AuthenticationPrincipal UserDetails user, @PathVariable int orderId) {
        ResponseData responseData = orderServiceImp.getUserOrdersDetail(user, orderId);
        return new ResponseEntity<>(responseData, responseData.getHttpStatus());
    }
}
