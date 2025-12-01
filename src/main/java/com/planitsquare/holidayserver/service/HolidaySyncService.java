package com.planitsquare.holidayserver.service;

import com.planitsquare.holidayserver.common.client.ApiClient;
import com.planitsquare.holidayserver.domain.Country;
import com.planitsquare.holidayserver.dto.CountryApiRes;
import com.planitsquare.holidayserver.dto.HolidayApiRes;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HolidaySyncService {
    static final int YEARS_TO_SUBTRACT = 5;

    private final ApiClient apiClient;
    private final HolidayService holidayService;
    private final CountryService countryService;

    public void syncAllCountriesAndHolidays() {
        List<CountryApiRes> res = apiClient.getCountries();
        for (Country c : countryService.saveCountries(res)) {
            syncCountryHoliday(c);
        }
    }

    private void syncCountryHoliday(Country country) {
        int end = LocalDate.now().getYear();
        int start = LocalDate.now().minusYears(YEARS_TO_SUBTRACT).getYear();
        for (int year = end; year > start; year--) {
            List<HolidayApiRes> holidays = apiClient.getHolidays(year, country.getCountryCode());
            holidayService.saveHolidays(country, holidays);
        }
    }
}
