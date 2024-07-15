package com.ecommerce.cozashop.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "size_option")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class SizeOption {

    @Id
    @GeneratedValue
    private Integer id;

    @NonNull
    private String value;

    @NonNull
    @ManyToOne
    @JoinColumn(name="size_category_id", nullable=false)
    @EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
    @ToString.Exclude   // Do not use this field in toString()
    private SizeCategory sizeCategory;

    @OneToMany(mappedBy = "sizeOption", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
    @ToString.Exclude   // Do not use this field in toString()
    private List<ProductSize> productSizes;
}
