package com.app.restaurant_management.commons.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FoodResponse {
    private Long id;
    private String foodName;
    private String description;
    private Double price;
    private String menuName;
    @JsonProperty("isDeleted")
    private Boolean isDeleted;
}
