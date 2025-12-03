package com.planitsquare.holidayserver.common.scheduler;

import com.planitsquare.holidayserver.service.HolidaySyncService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HolidayBatchScheduler {
    private static final int YEARS_TO_SUBTRACT_FOR_BATCH = 2;

    private final HolidaySyncService holidaySyncService;

    @Scheduled(cron = "0 0 1 2 1 *", zone = "Asia/Seoul")
    public void autoRefreshHolidays() {
        log.info("auto refresh holidays");
        int endYear = LocalDate.now().getYear();
        int startYear = LocalDate.now().minusYears(YEARS_TO_SUBTRACT_FOR_BATCH).getYear();
        holidaySyncService.syncAllCountriesAndHolidays(startYear, endYear);
        log.info("auto refresh holidays done");
    }
}
