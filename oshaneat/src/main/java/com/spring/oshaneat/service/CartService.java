package com.spring.oshaneat.service;

import com.spring.oshaneat.dto.CartDTO;
import com.spring.oshaneat.dto.FoodDTO;
import com.spring.oshaneat.entity.Cart;
import com.spring.oshaneat.entity.Food;
import com.spring.oshaneat.entity.Users;
import com.spring.oshaneat.payload.ResponseData;
import com.spring.oshaneat.payload.request.CartRequest;
import com.spring.oshaneat.repository.CartRepository;
import com.spring.oshaneat.repository.FoodRepository;
import com.spring.oshaneat.repository.UserRepository;
import com.spring.oshaneat.service.imp.CartServiceImp;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService implements CartServiceImp {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Override
    public ResponseData addCart(UserDetails userDetails, CartRequest cartRequest) {
        ResponseData responseData = new ResponseData();

        Optional<Users> usersOptional = userRepository.findByUsername(userDetails.getUsername());

        Food food = foodRepository.findById(cartRequest.getFoodId()).orElse(null);
        if (food == null) {
            responseData.setDescription("Food not found");
            responseData.setHttpStatus(HttpStatus.NOT_FOUND);
            return responseData;
        }

        Users user = usersOptional.get();

        // Kiểm tra nếu giỏ hàng đã có món ăn từ nhà hàng khác
        List<Cart> cartItems = cartRepository.findByUser(user);
        for (Cart item : cartItems) {
            if (item.getFood().getRestaurant().getId() != food.getRestaurant().getId()) {
                responseData.setDescription("Cannot add food from different restaurants");
                responseData.setHttpStatus(HttpStatus.CONFLICT);
                return responseData;
            }
        }

        Cart cartItem;

        // Kiểm tra xem món ăn đã tồn tại trong giỏ hàng của người dùng hay chưa
        Optional<Cart> existingCart = cartRepository.findByUserAndFood(user, food);
        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + cartRequest.getQuantity());
            cartItem = cartRepository.save(cart);
        } else {
            // Nếu chưa tồn tại, tạo một bản ghi mới trong giỏ hàng
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setFood(food);
            newCart.setQuantity(cartRequest.getQuantity());
            cartItem = cartRepository.save(newCart);
        }


        responseData.setData(convertCartItemFoodDTO(cartItem));
        responseData.setDescription("Food added to cart successfully");
        responseData.setHttpStatus(HttpStatus.OK);
        return responseData;
    }

    public ResponseData getUserCart(UserDetails userDetails) {
        ResponseData responseData = new ResponseData();
        Optional<Users> usersOptional = userRepository.findByUsername(userDetails.getUsername());

        CartDTO cartDTO = new CartDTO();
        List<Cart> cartItems = cartRepository.findByUser(usersOptional.get());
        List<FoodDTO> foodDTOs = cartItems.stream()
                .map(this::convertCartItemFoodDTO)
                .collect(Collectors.toList());

        double totalPrice = foodDTOs.stream()
                .mapToDouble(foodDTO -> foodDTO.getPrice() * foodDTO.getQuantity())
                .sum();

        cartDTO.setFoodDTOList(foodDTOs);
        cartDTO.setTotalPrice(totalPrice);
        responseData.setDescription("User cart items");
        responseData.setData(cartDTO);
        responseData.setHttpStatus(HttpStatus.OK);
        return responseData;
    }

    @Override
    @Transactional
    public ResponseData removeFoodInCart(UserDetails user, int foodId) {
        ResponseData responseData = new ResponseData();

        // Xác định người dùng
        Optional<Users> userOptional = userRepository.findByUsername(user.getUsername());

        Optional<Food> foodOptional = foodRepository.findById(foodId);
        if (foodOptional.isEmpty()) {
            responseData.setDescription("Food not found");
            responseData.setHttpStatus(HttpStatus.NOT_FOUND);
            return responseData;
        }

        // Xoá mục từ giỏ hàng
        cartRepository.deleteByUserAndFood(userOptional.get(), foodOptional.get());

        responseData.setDescription("Food removed from cart successfully");
        responseData.setHttpStatus(HttpStatus.OK);
        return responseData;
    }

    private FoodDTO convertCartItemFoodDTO(Cart cartItem) {
        Food food = cartItem.getFood();
        FoodDTO dto = new FoodDTO();
        dto.setId(food.getId());
        dto.setTitle(food.getTitle());
        dto.setImage(food.getImage());
        dto.setFreeShip(food.isFreeShip());
        dto.setPrice(food.getPrice());
        dto.setQuantity(cartItem.getQuantity());
        return dto;
    }
}
