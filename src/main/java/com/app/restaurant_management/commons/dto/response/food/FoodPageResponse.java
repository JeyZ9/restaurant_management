package com.app.restaurant_management.commons.dto.response.food;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FoodPageResponse {
    private List<FoodResponse> foodResponses;

    @Min(0)
    private Integer page;
    @Min(1)
    private Integer size;
    @Min(0)
    private Integer total;

    public FoodPageResponse(List<FoodResponse> foodResponses, Integer page, Integer size, Integer total){
        this.foodResponses = foodResponses;
        this.page = page;
        this.size = size;
        this.total = total;
    }
}
