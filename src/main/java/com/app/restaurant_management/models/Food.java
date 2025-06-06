package com.app.restaurant_management.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "food")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String foodName;

    private String description;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "id")
    private Menu menu;

    private Boolean isDeleted;

}
