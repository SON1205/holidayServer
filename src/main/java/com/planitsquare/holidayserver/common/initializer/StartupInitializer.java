package com.planitsquare.holidayserver.common.initializer;

import com.planitsquare.holidayserver.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StartupInitializer {
    private final HolidayService holidayService;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        holidayService.syncAllCountriesAndHolidays();
    }
}
