package com.planitsquare.holidayserver.service;

import com.planitsquare.holidayserver.common.client.ApiClient;
import com.planitsquare.holidayserver.domain.Country;
import com.planitsquare.holidayserver.dto.CountryApiRes;
import com.planitsquare.holidayserver.dto.HolidayApiRes;
import com.planitsquare.holidayserver.dto.HolidayRefreshReq;
import com.planitsquare.holidayserver.dto.RefreshResult;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HolidaySyncService {
    private final ApiClient apiClient;
    private final HolidayBatchService holidayBatchService;
    private final CountryService countryService;

    public void syncAllCountriesAndHolidays(int startYear, int endYear) {
        List<CountryApiRes> res = apiClient.getCountries();
        for (Country c : countryService.upsertCountries(res)) {
            syncCountryHoliday(c, startYear, endYear);
        }
    }

    private void syncCountryHoliday(Country country, int startYear, int endYear) {
        for (int year = startYear; year < endYear; year++) {
            List<HolidayApiRes> holidays = apiClient.getHolidays(year, country.getCountryCode());
            holidayBatchService.upsertHolidays(country, year, holidays);
        }
    }

    public RefreshResult refreshHoliday(HolidayRefreshReq request) {
        Country country = countryService.getCountryByCode(request.countryCode());
        List<HolidayApiRes> apiHolidays = apiClient.getHolidays(request.year(), country.getCountryCode());
        return holidayBatchService.upsertHolidays(country, request.year(), apiHolidays);
    }
}
