package com.example.order_service.services;

import com.example.order_service.client.UserClient;
import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.dto.UserResponse;
import com.example.order_service.entity.Order;
import com.example.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserClient userClient;

    public OrderResponse createOrder(OrderRequest orderRequest){

        // 1. Verify user exists by calling User Service via Feign
        UserResponse user = userClient.getUserById(orderRequest.getUserId());

        // 2. Save the order
        Order order = Order.builder()
                .userId(orderRequest.getUserId())
                .product(orderRequest.getProduct())
                .quantity(orderRequest.getQuantity())
                .build();
        Order saved = orderRepository.save(order);

        // 3. Return enriched response
        return OrderResponse.builder()
                .orderId(saved.getId())
                .product(saved.getProduct())
                .quantity(saved.getQuantity())
                .user(user)
                .build();
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));

        UserResponse user = userClient.getUserById(order.getUserId());

        return OrderResponse.builder()
                .orderId(order.getId())
                .product(order.getProduct())
                .quantity(order.getQuantity())
                .user(user)
                .build();
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> {
                    UserResponse user = userClient.getUserById(order.getUserId());
                    return OrderResponse.builder()
                            .orderId(order.getId())
                            .product(order.getProduct())
                            .quantity(order.getQuantity())
                            .user(user)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
