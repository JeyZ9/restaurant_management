package com.app.restaurant_management.commons.dto.response.food;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodResponse {
    private Long id;
    private String foodName;
    private String description;
    private Double price;
    private Long menuId;
    @JsonProperty("isDeleted")
    private Boolean isDeleted;
}
