package com.ecommerce.cozashop.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "shop_order")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ShopOrder {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    @EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
    @ToString.Exclude   // Do not use this field in toString()
    private User user;

    @NonNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate order_date;

    @NonNull
    private String shipping_address;

    @NonNull
    private Double order_total;

    @NonNull
    private Boolean status;

    @OneToMany(mappedBy = "shopOrder", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
    @ToString.Exclude   // Do not use this field in toString()
    private List<OrderLine> orderLines;
}
