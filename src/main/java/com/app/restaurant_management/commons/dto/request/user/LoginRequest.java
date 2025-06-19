package com.app.restaurant_management.commons.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "จำเป็นต้องป้อนชื่อผู้ใช้")
    private String username;

    @NotBlank(message = "จำเป็นต้องป้อนรหัสผ่าน")
    private String password;

}
