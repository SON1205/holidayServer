package com.planitsquare.holidayserver.service;

import com.planitsquare.holidayserver.domain.Country;
import com.planitsquare.holidayserver.domain.Holiday;
import com.planitsquare.holidayserver.dto.HolidayApiRes;
import com.planitsquare.holidayserver.dto.RefreshResult;
import com.planitsquare.holidayserver.repository.HolidayRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HolidayBatchService {
    private final HolidayRepository holidayRepository;

    @Transactional
    public RefreshResult upsertHolidays(Country country, Integer year, List<HolidayApiRes> apiHolidays) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        List<Holiday> existingList = holidayRepository.findAllByCountryAndDateBetween(country, startDate, endDate);

        Map<String, Holiday> nameMap = existingList.stream()
                .collect(Collectors.toMap(Holiday::getName, h -> h, (oldV, newV) -> oldV));

        long updated = 0;
        long inserted = 0;

        List<Holiday> newHolidays = new ArrayList<>();
        for (HolidayApiRes res : apiHolidays) {
            if (nameMap.containsKey(res.name())) {
                nameMap.get(res.name()).update(res);
                updated++;
                continue;
            }

            newHolidays.add(Holiday.fromApi(res, country));
            inserted++;
        }

        if (!newHolidays.isEmpty()) {
            holidayRepository.saveAll(newHolidays);
        }

        return RefreshResult.of(country.getCountryCode(), year, inserted, updated);
    }
}
