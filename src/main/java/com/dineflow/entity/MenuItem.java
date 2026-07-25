package com.dineflow.entity;

import com.dineflow.enums.Category;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "menu_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Double price;

    @Enumerated(EnumType.STRING)
    private Category category;

    private boolean veg;

    private boolean available;
    @OneToMany(mappedBy = "menuItem")
    private List<CartItem> cartItems;
}