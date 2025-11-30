package com.planitsquare.holidayserver.common.initializer;

import com.planitsquare.holidayserver.service.HolidaySyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StartupInitializer {
    private final HolidaySyncService holidaySyncService;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        log.info("loading holiday sync service");
        holidaySyncService.syncAllCountriesAndHolidays();
        log.info("loading holiday sync service done");
    }
}
