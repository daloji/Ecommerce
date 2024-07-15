package com.ecommerce.cozashop.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue
    @NonNull
    private Integer id;

    @NonNull
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    @EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
    @ToString.Exclude
    private User user;

    @NonNull
    @ManyToOne
    @JoinColumn(name="product_item_id", nullable=false)
    @EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
    @ToString.Exclude
    private ProductItem item;

    @NonNull
    private Integer qty;
}
