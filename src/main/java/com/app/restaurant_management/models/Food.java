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

    @Column(unique = true)
    private String foodName;

    private String image;

    private String description;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "id")
    private Menu menu;

    private Boolean isDeleted;

//    public Food(String foodName, String image, String description, Double price, Menu menu){
//        this.foodName = foodName;
//        this.image = image;
//        this.description = description;
//        this.price = price;
//        this.menu = menu;
//        this.isDeleted = false;
//    }

}
