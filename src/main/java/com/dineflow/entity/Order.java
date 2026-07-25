package com.dineflow.entity;

import com.dineflow.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    private String phoneNumber;

    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @JsonManagedReference
    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL)
    private List<OrderItem> items;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
