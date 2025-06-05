package com.app.restaurant_management.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Table(name = "users")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "จำเป็นต้องใส่ชื่อ")
    private String firstName;

    @NotBlank(message = "จำเป็นต้องใส่นามสกุล")
    private String lastName;

    @Email(message = "รูปแบบอีเมลไม่ถูกต้อง")
    @NotBlank(message = "จำเป็นต้องใส่อีเมล")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "จำเป็นต้องใส่เบอร์โทร")
    @Column(length = 10, unique = true)
    @Pattern(regexp = "\\d{10}", message = "เบอร์โทรต้องมี 10 หลัก")
    private String phoneNumber;

    @NotBlank(message = "จำเป็นต้องใส่ชื่อผู้ใช้")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "จำเป็นต้องใส่รหัสผ่าน")
    @JsonIgnore
    @Pattern(
            regexp = "^(?=.*[A-Za-z]).{8,}$",
            message = "รหัสผ่านต้องมีอย่างน้อย 8 ตัวอักษร และต้องมีตัวอักษรอย่างน้อย 1 ตัว"
    )
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();
}
