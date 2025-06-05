package com.app.restaurant_management.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    private String foodName;

    private String description;

    @NotNull(message = "จำเป็นต้องใส่ราคา")
    @DecimalMin(value = "0.0", inclusive = true, message = "ราคาต้องมากกว่าหรือเท่ากับ 0")
    private Double price;

    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "id")
    private Menu menu;

    @JsonProperty("is_deleted")
    private boolean isDeleted;

}
