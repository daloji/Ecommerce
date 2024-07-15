package com.ecommerce.cozashop.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_size")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ProductSize {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name="product_item_id", nullable=false)
    @EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
    @ToString.Exclude   // Do not use this field in toString()
    private ProductItem productItem;

    @NonNull
    @ManyToOne
    @JoinColumn(name="size_option_id", nullable=false)
    @EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
    @ToString.Exclude   // Do not use this field in toString()
    private SizeOption sizeOption;
}
