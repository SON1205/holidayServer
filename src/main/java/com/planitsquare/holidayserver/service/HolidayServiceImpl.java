package com.planitsquare.holidayserver.service;

import static com.planitsquare.holidayserver.common.exception.ExceptionCode.NOT_FOUND_COUNTRY_CODE;

import com.planitsquare.holidayserver.common.exception.NotFoundException;
import com.planitsquare.holidayserver.domain.Country;
import com.planitsquare.holidayserver.domain.Holiday;
import com.planitsquare.holidayserver.dto.HolidayRemoveReq;
import com.planitsquare.holidayserver.dto.HolidayResponse;
import com.planitsquare.holidayserver.dto.HolidaySearchCondition;
import com.planitsquare.holidayserver.repository.CountryRepository;
import com.planitsquare.holidayserver.repository.HolidayRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService {
    private final HolidayRepository holidayRepository;
    private final CountryRepository countryRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<HolidayResponse> searchHolidays(HolidaySearchCondition condition, Pageable pageable) {
        countryRepository.findByCountryCode(condition.countryCode())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COUNTRY_CODE));
        Page<Holiday> searched = holidayRepository.search(condition, pageable);
        return searched.map(HolidayResponse::from);
    }

    @Transactional
    @Override
    public void removeHoliday(HolidayRemoveReq request) {
        Country country = countryRepository.findByCountryCode(request.countryCode())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COUNTRY_CODE));
        LocalDate startDate = LocalDate.of(request.year(), 1, 1);
        LocalDate endDate = LocalDate.of(request.year(), 12, 31);

        holidayRepository.removeHolidayByCountryAndDateBetween(country, startDate, endDate);
    }
}
