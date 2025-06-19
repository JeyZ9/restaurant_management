package com.app.restaurant_management.config;

import com.app.restaurant_management.commons.constants.PathConstants;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Restaurant Management",
                description = "This is a Restaurant Management APIs",
                version = "1.0",
                contact = @Contact(
                        name = "JeyZ9",
                        email = "ggjj4511@gmail.com",
                        url = "https://github.com/jeyz9"
                ),
                license = @License(
                        name = "Apache License 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        security = @SecurityRequirement(name = "Bearer Authentication")
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Restaurant Management APIs")
                        .version("1.0")
                        .description("Comprehensive APIs documentation for the Restaurant Management")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("JeyZ9")
                                .email("ggjj4511@gmail.com")
                                .url("https://github.com/JeyZ9")
                        )
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("Apache License 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")
                        )
                )
                .externalDocs(new ExternalDocumentation()
                        .description("GitHub Repository")
                        .url("https://github.com/JeyZ9/restaurant_management.git")
                );
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("Public APIs")
                .pathsToMatch(PathConstants.API_V1+"/**")
                .build();
    }
}
