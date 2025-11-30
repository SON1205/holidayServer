package com.planitsquare.holidayserver.service;

import com.planitsquare.holidayserver.common.client.ApiClient;
import com.planitsquare.holidayserver.domain.Country;
import com.planitsquare.holidayserver.domain.Holiday;
import com.planitsquare.holidayserver.dto.CountryApiRes;
import com.planitsquare.holidayserver.dto.HolidayApiRes;
import com.planitsquare.holidayserver.repository.CountryRepository;
import com.planitsquare.holidayserver.repository.HolidayRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService {
    private final ApiClient apiClient;
    private final HolidayRepository holidayRepository;
    private final CountryRepository countryRepository;

    @Transactional
    public void syncAllCountriesAndHolidays() {
        List<CountryApiRes> countries = apiClient.getCountries();
        List<Country> savedCountries = countryRepository.saveAll(
                countries.stream().map(r -> Country.of(r.countryCode(), r.name())).toList()
        );

        LocalDate now = LocalDate.now();
        int end = now.getYear();
        int start = now.minusYears(5).getYear();
        for (Country c : savedCountries) {
            for (int i = end; i > start; i--) {
                List<HolidayApiRes> holidays = apiClient.getHolidays(i, c.getCountryCode());
                holidayRepository.saveAll(
                        holidays.stream()
                                .map(h -> Holiday.builder()
                                        .date(h.date())
                                        .localName(h.localName())
                                        .name(h.name())
                                        .country(c)
                                        .fixed(h.fixed())
                                        .global(h.global())
                                        .launchYear(h.launchYear())
                                        .counties(h.counties())
                                        .types(h.types())
                                        .build()
                                ).toList()
                );
            }
        }
    }
}
