package com.app.restaurant_management.models;

import com.app.restaurant_management.commons.enums.RoleName;
import jakarta.persistence.*;
import lombok.Getter;

@Table(name = "roles")
@Entity
@Getter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private RoleName roleName;
}
