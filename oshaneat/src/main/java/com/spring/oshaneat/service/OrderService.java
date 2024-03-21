package com.spring.oshaneat.service;

import com.spring.oshaneat.dto.FoodDTO;
import com.spring.oshaneat.dto.OrderDTO;
import com.spring.oshaneat.entity.*;
import com.spring.oshaneat.entity.keys.KeyOrderItem;
import com.spring.oshaneat.payload.ResponseData;
import com.spring.oshaneat.repository.CartRepository;
import com.spring.oshaneat.repository.OrderItemRepository;
import com.spring.oshaneat.repository.OrderRepository;
import com.spring.oshaneat.repository.UserRepository;
import com.spring.oshaneat.service.imp.OrderServiceImp;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class OrderService implements OrderServiceImp {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseData insertOrder(UserDetails userDetails) {
        ResponseData responseData = new ResponseData();

        // Lấy thông tin người dùng
        Optional<Users> userOptional = userRepository.findByUsername(userDetails.getUsername());
        Users user = userOptional.get();

        // Lấy giỏ hàng của người dùng
        List<Cart> cartItems = cartRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            responseData.setDescription("Cart is empty");
            responseData.setHttpStatus(HttpStatus.BAD_REQUEST);
            return responseData;
        }

        Restaurant restaurant = cartItems.get(0).getFood().getRestaurant();

        // Tạo đơn hàng mới
        Orders order = new Orders();
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setCreateDate(new Date());
        order.setAddress(user.getAddress());
        order = orderRepository.save(order);

        // Chuyển các mục trong giỏ hàng thành các mục đơn hàng
        for (Cart cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            KeyOrderItem keys = new KeyOrderItem(order.getId(), cartItem.getFood().getId());
            orderItem.setKeys(keys);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getFood().getPrice());
            orderItemRepository.save(orderItem);
        }

        // Xóa giỏ hàng
        cartRepository.deleteAll(cartItems);

        responseData.setDescription("Order placed successfully");
        responseData.setHttpStatus(HttpStatus.OK);
        return responseData;
    }

    @Override
    public ResponseData getUserOrders(UserDetails userDetails) {
        ResponseData responseData = new ResponseData();

        // Lấy thông tin người dùng
        Optional<Users> userOptional = userRepository.findByUsername(userDetails.getUsername());
        Users user = userOptional.get();

        // Lấy giỏ hàng của người dùng
        List<Orders> orders = orderRepository.findByUser(user);

        if (!orders.isEmpty()) {
            List<OrderDTO> orderDTOs = new ArrayList<>();
            for (Orders order : orders) {
                OrderDTO orderDTO = new OrderDTO();

                // Set thông tin restaurant
                orderDTO.setOrderId(order.getId());
                orderDTO.setRestaurant(order.getRestaurant().getTitle());
                orderDTO.setAddress(order.getAddress());

                List<String> category = new ArrayList<>();

                for (OrderItem orderItem: order.getListOrderItems()) {
                    category.add(orderItem.getFood().getCategory().getNameCate());
                }

                orderDTO.setCategoryList(category);

                // Set ngày tạo đơn hàng
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                orderDTO.setDate(sdf.format(order.getCreateDate()));

                orderDTOs.add(orderDTO);
            }
            responseData.setData(orderDTOs);
        }

        responseData.setHttpStatus(HttpStatus.OK);
        return responseData;
    }

    @Override
    public ResponseData getUserOrdersDetail(UserDetails userDetails, int orderId) {
        ResponseData responseData = new ResponseData();

        // Lấy thông tin người dùng
        Optional<Users> userOptional = userRepository.findByUsername(userDetails.getUsername());
        Users user = userOptional.get();

        // Lấy giỏ hàng của người dùng
        Optional<Orders> ordersOptional = orderRepository.findByIdAndUser(orderId, user);

        if (ordersOptional.isPresent()) {
            Orders order = ordersOptional.get();
            OrderDTO orderDTO = new OrderDTO();

            // Set thông tin restaurant
            orderDTO.setRestaurant(order.getRestaurant().getTitle());

            // Set address
            orderDTO.setAddress(order.getAddress());

            // Xử lý và set danh sách FoodDTO từ OrderItems
            List<FoodDTO> foodDTOList = new ArrayList<>();

            for (OrderItem orderItem: order.getListOrderItems()) {
                FoodDTO foodDTO = new FoodDTO();
                foodDTO.setTitle(orderItem.getFood().getTitle());
                foodDTO.setPrice(orderItem.getPrice());
                foodDTO.setQuantity(orderItem.getQuantity());

                foodDTOList.add(foodDTO);
            }

            orderDTO.setFoodDTOList(foodDTOList);

            // Tính và set tổng tiền
            long total = foodDTOList.stream()
                    .mapToLong(foodDTO ->
                            Math.round(foodDTO.getPrice() * foodDTO.getQuantity()))
                    .sum();
            orderDTO.setTotal(total);

            responseData.setHttpStatus(HttpStatus.OK);
            responseData.setData(orderDTO);
        } else {
            responseData.setHttpStatus(HttpStatus.BAD_REQUEST);
            responseData.setDescription("Invalid order id");
        }
        return responseData;
    }
}
