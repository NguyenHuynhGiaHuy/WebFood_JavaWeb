package com.spring.oshaneat.service.imp;

import com.spring.oshaneat.payload.ResponseData;
import com.spring.oshaneat.payload.request.CartRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface CartServiceImp {
    ResponseData addCart(UserDetails userDetails, CartRequest cartRequest);

    ResponseData getUserCart(UserDetails user);

    ResponseData removeFoodInCart(UserDetails user, int foodId);
}
