package com.ecommerce.cozashop.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "size_category")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class SizeCategory {

    @Id
    @GeneratedValue
    private Integer id;

    @NonNull
    private String name_size;

    @NonNull
    @ManyToOne
    @JoinColumn(name="category_id", nullable=false)
    @EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
    @ToString.Exclude   // Do not use this field in toString()
    private ProductCategory productCategory;


    @OneToMany(mappedBy = "sizeCategory", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
    @ToString.Exclude   // Do not use this field in toString()
    private List<SizeOption> sizeOptions;
}
