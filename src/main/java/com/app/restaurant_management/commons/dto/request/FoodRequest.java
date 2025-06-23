package com.app.restaurant_management.commons.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class FoodRequest {

    private Long id;

    @NotBlank(message = "จำเป็นต้องใส่ชื่ออาหาร")
    private String foodName;

    private String description;

    @NotNull(message = "จำเป็นต้องใส่ราคา")
    @DecimalMin(value = "0.0", inclusive = true, message = "ราคาต้องมากกว่าหรือเท่ากับ 0")
    private Double price;

//    @NotBlank(message = "จำเป็นต้องเพิ่มรูปภาพ")
//    private String image;

    @NotNull(message = "จำเป็นต้องใส่รหัสเมนู")
    private Long menuId;

    @JsonProperty("is_deleted")
    private Boolean isDeleted = false;

//    @Override
//    public MultipartFile getImage(){
//        return image;
//    }

    public FoodRequest (String foodName, String description, Double price, Long menuId){
        this.id = null;
        this.foodName = foodName;
        this.description = description;
        this.price = price;
        this.menuId = menuId;
        this.isDeleted = false;
    }
}
