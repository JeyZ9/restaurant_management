package com.app.restaurant_management.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "menu")
@Entity
@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "จำเป็นต้องใส่ชื่อเมนู")
    private String menuName;

    public Menu(String menuName) {
        this.menuName = menuName;
    }
}
