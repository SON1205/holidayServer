package com.planitsquare.holidayserver.common.scheduler;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.planitsquare.holidayserver.service.HolidaySyncService;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("기능5(배치 자동화)")
class HolidayBatchSchedulerTest {

    @Test
    @DisplayName("스케줄러가 실행되면 금년도와 과거 2년치를 계산해서 서비스 호출")
    void autoRefreshHoliday() {
        // given
        HolidaySyncService holidaySyncService = mock(HolidaySyncService.class);
        HolidayBatchScheduler scheduler = new HolidayBatchScheduler(holidaySyncService);

        // when
        // @Scheduled 어노테이션 무시하고 메서드를 직접 호출 (강제 실행)
        scheduler.autoRefreshHolidays();

        // then
        int currentYear = LocalDate.now().getYear();
        int expectedStartYear = currentYear - 2;

        // verify: 서비스가 정확한 파라미터로 호출되었는지 검증
        then(holidaySyncService).should().syncAllCountriesAndHolidays(expectedStartYear, currentYear);
    }
}