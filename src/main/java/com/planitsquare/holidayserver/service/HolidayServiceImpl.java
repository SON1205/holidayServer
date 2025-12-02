package com.planitsquare.holidayserver.service;

import com.planitsquare.holidayserver.domain.Country;
import com.planitsquare.holidayserver.domain.Holiday;
import com.planitsquare.holidayserver.dto.HolidayApiRes;
import com.planitsquare.holidayserver.dto.HolidayResponse;
import com.planitsquare.holidayserver.dto.HolidaySearchCondition;
import com.planitsquare.holidayserver.repository.HolidayRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService {
    private final HolidayRepository holidayRepository;

    @Transactional
    @Override
    public List<Holiday> saveHolidays(Country c, List<HolidayApiRes> holidays) {
        return holidayRepository.saveAll(
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
                                .build())
                        .toList()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Page<HolidayResponse> searchHolidays(HolidaySearchCondition condition, Pageable pageable) {
        // 입력값 검증

        // 데이터 찾기
        Page<Holiday> searched = holidayRepository.search(condition, pageable);
        return new PageImpl<>(
                searched.getContent().stream().map(HolidayResponse::from).toList(),
                pageable,
                searched.getTotalElements()
        );
    }
}
