package com.ecommerce.cozashop.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity
@Table(name = "order_line")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class OrderLine {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "product_item_id", nullable = false)
    @EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
    @ToString.Exclude   // Do not use this field in toString()
    private ProductItem productItem;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "shop_order_id", nullable = false)
    @EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
    @ToString.Exclude   // Do not use this field in toString()
    private ShopOrder shopOrder;

    @NonNull
    private Integer qty;

    @NonNull
    private Double price;
}
