package com.example.order_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString @Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // We only store the userId (not the full User object)
    // The full user details are fetched live from User Service via Feign
    private Long userId;

    private String product;
    private int quantity;
}
