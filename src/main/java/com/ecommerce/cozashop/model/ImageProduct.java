package com.ecommerce.cozashop.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "image_product")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ImageProduct {

    @Id
    @GeneratedValue
    private Integer id;

    @NonNull
    private String img_url;

    @NonNull
    @ManyToOne
    @JoinColumn(name="product_item_id", nullable=false)
    @EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
    @ToString.Exclude   // Do not use this field in toString()
    private ProductItem productItem;
}
