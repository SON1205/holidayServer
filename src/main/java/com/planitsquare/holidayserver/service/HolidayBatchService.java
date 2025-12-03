package com.planitsquare.holidayserver.service;

import com.planitsquare.holidayserver.domain.Country;
import com.planitsquare.holidayserver.domain.Holiday;
import com.planitsquare.holidayserver.dto.HolidayApiRes;
import com.planitsquare.holidayserver.dto.RefreshResult;
import com.planitsquare.holidayserver.repository.HolidayRepository;
import java.time.LocalDate;
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
    public List<Holiday> saveHolidays(Country country, List<HolidayApiRes> holidays) {
        return holidayRepository.saveAll(
                holidays.stream()
                        .map(apiRes -> Holiday.fromApi(apiRes, country))
                        .toList()
        );
    }

    @Transactional
    public RefreshResult upsertHolidays(Country country, Integer year, List<HolidayApiRes> apiHolidays) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        List<Holiday> existingList = holidayRepository.findAllByCountryAndDateBetween(country, startDate, endDate);

        // 2. Map 변환 (성능 최적화)
        Map<LocalDate, Holiday> dateMap = existingList.stream()
                .collect(Collectors.toMap(Holiday::getDate, h -> h));
        Map<String, Holiday> nameMap = existingList.stream()
                .collect(Collectors.toMap(Holiday::getName, h -> h, (oldV, newV) -> oldV));

        long updated = 0;
        long inserted = 0;

        for (HolidayApiRes res : apiHolidays) {
            // 날짜 일치 -> Update
            if (dateMap.containsKey(res.date())) {
                dateMap.get(res.date()).update(res);
                updated++;
                continue;
            }
            // 이름 일치 -> Update (날짜 변경 대응)
            if (nameMap.containsKey(res.name())) {
                nameMap.get(res.name()).update(res);
                updated++;
                continue;
            }

            holidayRepository.save(Holiday.fromApi(res, country));
            inserted++;
        }

        return RefreshResult.of(country.getCountryCode(), year, inserted, updated);
    }
}
