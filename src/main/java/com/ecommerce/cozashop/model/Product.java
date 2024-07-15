package com.ecommerce.cozashop.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String product_name;

    @NonNull
    private String description;

    @NonNull
    private String product_img;

    @NonNull
    private Boolean status;

    @NonNull
    @ManyToOne
    @JoinColumn(name="category_id", nullable=false)
    @EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
    @ToString.Exclude   // Do not use this field in toString()
    private ProductCategory productCategory;
}
