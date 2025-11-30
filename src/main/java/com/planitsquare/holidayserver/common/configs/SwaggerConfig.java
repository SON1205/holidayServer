package com.planitsquare.holidayserver.common.configs;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(info());
    }

    private Info info() {
        return new Info()
                .title("플랜잇스퀘어 백엔드 개발자 채용 과제")
                .description("Holiday Keeper - Nager.Date 무인증 API 활용")
                .version("1.0.0");
    }
}
