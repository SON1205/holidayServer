package com.planitsquare.holidayserver.common.client;

import com.planitsquare.holidayserver.dto.CountryApiRes;
import com.planitsquare.holidayserver.dto.HolidayApiRes;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class ApiClient {
    private final RestClient restClient;

    public List<CountryApiRes> getCountries() {
        return restClient.get()
                .uri("/AvailableCountries")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public List<HolidayApiRes> getHolidays(int year, String country) {
        return restClient.get()
                .uri("/PublicHolidays/{year}/{country}", year, country)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
