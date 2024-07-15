package com.ecommerce.cozashop.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "product_category")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ProductCategory {

    @Id
    @GeneratedValue
    private Integer id;

    @NonNull
    private String category_name;

    @OneToMany(mappedBy = "productCategory")
    @EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
    @ToString.Exclude   // Do not use this field in toString()
    private List<Product> products;

    @OneToMany(mappedBy = "productCategory")
    @EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
    @ToString.Exclude   // Do not use this field in toString()
    private List<SizeCategory> sizeCategories;
}
