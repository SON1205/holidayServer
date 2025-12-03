package com.planitsquare.holidayserver.common.initializer;

import com.planitsquare.holidayserver.service.HolidaySyncService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StartupInitializer {
    private static final int YEARS_TO_SUBTRACT_FOR_INIT = 5;

    private final HolidaySyncService holidaySyncService;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        log.info("loading holiday sync service");
        int endYear = LocalDate.now().getYear();
        int startYear = LocalDate.now().minusYears(YEARS_TO_SUBTRACT_FOR_INIT).getYear();
        holidaySyncService.syncAllCountriesAndHolidays(startYear, endYear);
        log.info("loading holiday sync service done");
    }
}
