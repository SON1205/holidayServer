package com.planitsquare.holidayserver.service;

import com.planitsquare.holidayserver.domain.Holiday;
import com.planitsquare.holidayserver.dto.HolidayResponse;
import com.planitsquare.holidayserver.dto.HolidaySearchCondition;
import com.planitsquare.holidayserver.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService {
    private final HolidayRepository holidayRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<HolidayResponse> searchHolidays(HolidaySearchCondition condition, Pageable pageable) {
        Page<Holiday> searched = holidayRepository.search(condition, pageable);
        return searched.map(HolidayResponse::from);
    }
}
