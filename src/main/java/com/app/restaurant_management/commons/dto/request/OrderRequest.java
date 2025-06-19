package com.app.restaurant_management.commons.dto.request;

import com.app.restaurant_management.commons.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @NotNull(message = "จำเป็นต้องใส่รหัสของลูกค้า")
    private Long user;

    @NotNull(message = "จำเป็นต้องใส่วันที่")
    @FutureOrPresent(message = "วันที่ต้องเป็นปัจจุบันหรืออนาคตเท่านั้น")
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "จำเป็นต้องใส่สถานะของออร์เดอร์")
    private OrderStatus orderStatus;

    @NotNull(message = "จำเป็นต้องใส่รหัสของโต๊ะ")
    private Long tables;
}
