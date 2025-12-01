package com.planitsquare.holidayserver.common.configs;

import java.net.http.HttpClient;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    private static final String BASE_URL = "https://date.nager.at/api/v3";
    private static final int CONNECTION_TIMEOUT_SECONDS = 3;
    private static final int READ_TIMEOUT_SECONDS = 15;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .requestFactory(jdkClientHttpRequestFactory())
                .baseUrl(BASE_URL)
                .build();
    }

    private static HttpClient httpClient() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(CONNECTION_TIMEOUT_SECONDS))
                .build();
    }

    private static JdkClientHttpRequestFactory jdkClientHttpRequestFactory() {
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient());
        requestFactory.setReadTimeout(Duration.ofSeconds(READ_TIMEOUT_SECONDS));
        return requestFactory;
    }
}
