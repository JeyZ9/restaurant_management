package com.app.restaurant_management.commons.dto.request.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "จำเป็นต้องใส่ชื่อ")
    private String firstName;

    @NotBlank(message = "จำเป็นต้องใส่นามสกุล")
    private String lastName;

    @Email(message = "รูปแบบอีเมลไม่ถูกต้อง")
    @NotBlank(message = "จำเป็นต้องใส่อีเมล")
    private String email;

    @NotBlank(message = "จำเป็นต้องใส่เบอร์โทร")
    @Pattern(regexp = "\\d{10}", message = "เบอร์โทรต้องมี 10 หลัก")
    private String phoneNumber;

    @NotBlank(message = "จำเป็นต้องใส่ชื่อผู้ใช้")
    private String username;

    @NotBlank(message = "จำเป็นต้องใส่รหัสผ่าน")
    @Pattern(
            regexp = "^(?=.*[A-Za-z]).{8,}$",
            message = "รหัสผ่านต้องมีอย่างน้อย 8 ตัวอักษร และต้องมีตัวอักษรอย่างน้อย 1 ตัว"
    )
    private String password;

    @NotBlank(message = "จำเป็นต้องยืนยันรหัสผ่าน")
    private String ConfirmPassword;

    @JsonProperty("is_admin")
    private Boolean isAdmin;
}
