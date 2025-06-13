package com.app.restaurant_management.commons.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
}
