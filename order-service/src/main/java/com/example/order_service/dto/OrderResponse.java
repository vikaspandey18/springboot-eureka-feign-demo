package com.example.order_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long orderId;
    private String product;
    private int quantity;
    private UserResponse user;    // Enriched with user details fetched via Feign
}