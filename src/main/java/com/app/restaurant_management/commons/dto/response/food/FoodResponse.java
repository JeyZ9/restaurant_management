package com.app.restaurant_management.commons.dto.response.food;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FoodResponse {
    private Long id;
    private String foodName;
    private String description;
    private Double price;
    private String menuName;
    @JsonProperty("isDeleted")
    private Boolean isDeleted;
}
