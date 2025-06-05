package com.app.restaurant_management.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "tables")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tables {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "จำนวนที่นั่งห้ามเป็นค่าว่าง")
    @Min(value = 1, message = "จำนวนที่นั่งห้ามน้อยกว่า 1 ที่นั่ง")
    private Integer numberOfSeats;

    @JsonProperty("isAvailable")
    private boolean isAvailable;
}
