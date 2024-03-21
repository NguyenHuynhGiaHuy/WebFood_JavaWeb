package com.spring.oshaneat.service.imp;

import com.spring.oshaneat.payload.ResponseData;
import org.springframework.security.core.userdetails.UserDetails;

public interface OrderServiceImp {
    ResponseData insertOrder(UserDetails user);
    ResponseData getUserOrders(UserDetails user);
    ResponseData getUserOrdersDetail(UserDetails user, int orderId);
}
