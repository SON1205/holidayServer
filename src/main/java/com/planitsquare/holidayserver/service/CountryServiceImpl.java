package com.planitsquare.holidayserver.service;

import static com.planitsquare.holidayserver.common.exception.ExceptionCode.NOT_FOUND_COUNTRY_CODE;

import com.planitsquare.holidayserver.common.exception.NotFoundException;
import com.planitsquare.holidayserver.domain.Country;
import com.planitsquare.holidayserver.dto.CountryApiRes;
import com.planitsquare.holidayserver.repository.CountryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Transactional
    @Override
    public List<Country> upsertCountries(List<CountryApiRes> countryApiResList) {
        Map<String, Country> existingMap = countryRepository.findAll().stream()
                .collect(Collectors.toMap(Country::getCountryCode, c -> c));

        List<Country> result = new ArrayList<>();
        List<Country> newCountries = new ArrayList<>();
        for (CountryApiRes res : countryApiResList) {
            if (!existingMap.containsKey(res.countryCode())) {
                Country newCountry = Country.of(res.countryCode(), res.name());
                newCountries.add(newCountry);
                result.add(newCountry);
                continue;
            }

            Country country = existingMap.get(res.countryCode());
            if (!country.getName().equals(res.name())) {
                country.updateName(res.name());
            }
            result.add(country);
        }

        if (!newCountries.isEmpty()) {
            countryRepository.saveAll(newCountries);
        }

        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public Country getCountryByCode(String code) {
        return countryRepository.findByCountryCode(code)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COUNTRY_CODE));
    }
}
