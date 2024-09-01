package com.ecommerce.cozashop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

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
    private DeliveryStatus status;

    @NonNull
    private Integer qty;

    @NonNull
    private Double price;
}
