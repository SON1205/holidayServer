package com.planitsquare.holidayserver.service;

import com.planitsquare.holidayserver.domain.Country;
import com.planitsquare.holidayserver.dto.CountryApiRes;
import java.util.List;

public interface CountryService {
    List<Country> upsertCountries(List<CountryApiRes> countries);

    Country getCountryByCode(String code);
}
