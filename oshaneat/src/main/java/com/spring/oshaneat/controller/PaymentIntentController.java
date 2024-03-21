package com.spring.oshaneat.controller;

import com.spring.oshaneat.dto.CartDTO;
import com.spring.oshaneat.dto.FoodDTO;
import com.spring.oshaneat.dto.PaymentDTO;
import com.spring.oshaneat.dto.UserDTO;
import com.spring.oshaneat.payload.ResponseData;
import com.spring.oshaneat.service.imp.CartServiceImp;
import com.spring.oshaneat.service.imp.UserServiceImp;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
public class PaymentIntentController {

    @Value("${stripe.api.secretKey}")
    private String secretKey;

    @Autowired
    CartServiceImp cartServiceImp;

    @Autowired
    UserServiceImp userServiceImp;

    @PostConstruct
    public void  initSecretKey(){
        Stripe.apiKey = secretKey;
    }

    @PostMapping("/create-payment-intent")
    public PaymentDTO createPaymentIntent(@AuthenticationPrincipal UserDetails user)
            throws StripeException {

        ResponseData responseData = cartServiceImp.getUserCart(user);
        UserDTO userDTO = (UserDTO) userServiceImp.getUser(user).getData();

        CartDTO cartDTO = (CartDTO) responseData.getData();
        if(cartDTO.getFoodDTOList().isEmpty()) {
            return new PaymentDTO("0", "0");
        }
        if(userDTO.getAddress() == null || userDTO.getAddress().isEmpty()) {
            return new PaymentDTO("-1", "-1");
        }

        CustomerCreateParams customerParams = CustomerCreateParams.builder()
                .setEmail(user.getUsername())
                .build();
        Customer customer = Customer.create(customerParams);

        Map<String, String> metadata = new HashMap<>();
        for (FoodDTO food : cartDTO.getFoodDTOList()) {
            metadata.put("item_" + food.getId(), food.getTitle());
        }

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount((long) cartDTO.getTotalPrice())
                        .setCurrency("vnd")
                        .setCustomer(customer.getId())
                        .putAllMetadata(metadata)
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .build();
        PaymentIntent intent = PaymentIntent.create(params);
        return new PaymentDTO(intent.getId(), intent.getClientSecret());
    }
}
