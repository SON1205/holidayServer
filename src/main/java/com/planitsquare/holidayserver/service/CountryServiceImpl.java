package com.planitsquare.holidayserver.service;

import static com.planitsquare.holidayserver.common.exception.ExceptionCode.NOT_FOUND_COUNTRY_CODE;

import com.planitsquare.holidayserver.common.exception.NotFoundException;
import com.planitsquare.holidayserver.domain.Country;
import com.planitsquare.holidayserver.dto.CountryApiRes;
import com.planitsquare.holidayserver.repository.CountryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Transactional
    @Override
    public List<Country> saveCountries(List<CountryApiRes> countries) {
        return countryRepository.saveAll(
                countries.stream()
                        .map(r -> Country.of(r.countryCode(), r.name()))
                        .toList()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Country getCountryByCode(String code) {
        return countryRepository.findByCountryCode(code)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COUNTRY_CODE));
    }
}
