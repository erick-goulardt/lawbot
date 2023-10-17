package com.ifsul.lawbot.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(security = {@SecurityRequirement(name = "RequestedBy"), @SecurityRequirement(name = "JWT")})
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("JWT", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .name("Authorization")
                                .in(SecurityScheme.In.HEADER)
                                .description("Insira o token JWT com o Bearer. Por exemplo: `Bearer abcde12345 `."))
                        .addSecuritySchemes("RequestedBy", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .name("RequestedBy")
                                .in(SecurityScheme.In.HEADER)
                                .description("Insira o userId")))
                .info(new Info()
                        .title("Lawbot - JGui & Érick")
                        .description("Documentação Swagger do Lawbot")
                        .version("1.0.0"));
    }
}
