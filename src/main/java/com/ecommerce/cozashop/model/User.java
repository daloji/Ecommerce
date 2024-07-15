package com.ecommerce.cozashop.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(name = "user_unique", columnNames = {"email", "phone"}))
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "first_name")
    private String first_name;

    @NonNull
    private String last_name;

    @NonNull
    @Column(name = "email")
    private String email;

    @NonNull
    private String phone;

    @NonNull
    private String password;

    @NonNull
    private Boolean status;

    @NonNull
    @ManyToOne
    @JoinColumn(name="role_id", nullable=false)
    @EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
    @ToString.Exclude   // Do not use this field in toString()
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
    @ToString.Exclude   // Do not use this field in toString()
    private List<ShopOrder> shopOrders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<CartItem> cartItems;
    
}
