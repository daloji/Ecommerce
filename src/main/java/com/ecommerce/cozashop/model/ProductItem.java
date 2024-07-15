package com.ecommerce.cozashop.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "product_item")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ProductItem {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String SKU;

    @NonNull
    private Integer qty_in_stock;

    @NonNull
    private String product_img;

    @NonNull
    private Double price;

    @NonNull
    @ManyToOne
    @JoinColumn(name="product_id", nullable=false)
    @EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
    @ToString.Exclude   // Do not use this field in toString()
    private Product product_id;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<CartItem> cartItems;

    @OneToMany(mappedBy = "productItem", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
    @ToString.Exclude   // Do not use this field in toString()
    private List<ProductSize> productSizes;

    @OneToMany(mappedBy = "productItem", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
    @ToString.Exclude   // Do not use this field in toString()
    private List<OrderLine> orderLines;

    @OneToMany(mappedBy = "productItem", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
    @ToString.Exclude   // Do not use this field in toString()
    private List<ImageProduct> imageProducts;



}
