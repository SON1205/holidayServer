package com.planitsquare.holidayserver.service;

import com.planitsquare.holidayserver.common.client.ApiClient;
import com.planitsquare.holidayserver.domain.Country;
import com.planitsquare.holidayserver.dto.CountryApiRes;
import com.planitsquare.holidayserver.dto.HolidayApiRes;
import com.planitsquare.holidayserver.dto.HolidayRefreshReq;
import com.planitsquare.holidayserver.dto.RefreshResult;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HolidaySyncService {
    private static final int YEARS_TO_SUBTRACT = 5;

    private final ApiClient apiClient;
    private final HolidayBatchService holidayBatchService;
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
            holidayBatchService.saveHolidays(country, holidays);
        }
    }

    public RefreshResult refreshHoliday(HolidayRefreshReq request) {
        Country country = countryService.getCountryByCode(request.countryCode());
        List<HolidayApiRes> apiHolidays = apiClient.getHolidays(request.year(), country.getCountryCode());
        return holidayBatchService.upsertHolidays(country, request.year(), apiHolidays);
    }
}
