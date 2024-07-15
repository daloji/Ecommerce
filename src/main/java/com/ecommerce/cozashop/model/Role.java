package com.ecommerce.cozashop.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "role")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Role {
    @Id
    @GeneratedValue
    private Integer id;

    @NonNull
    private String role_name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude // Do not use this field in equals and hashcode
    @ToString.Exclude   // Do not use this field in toString()
    private List<User> user;

}
