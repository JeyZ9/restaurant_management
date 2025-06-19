package com.app.restaurant_management.commons.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {
    @NotNull(message = "จำเป็นต้องใส่รหัสขของออร์เดอร์")
    private Long orders;

    @NotNull(message = "จำเป็นต้องใส่รหัสของอาหาร")
    private Long foods;

    @NotNull(message = "จำเป็นต้องใส่จำนวนอาหาร")
    @Positive(message = "จำนวนอาหารต้องมีค่ามากกว่า 0")
    private Integer quantity;
}
