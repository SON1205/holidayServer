package com.planitsquare.holidayserver.service;

import com.planitsquare.holidayserver.dto.HolidayRemoveReq;
import com.planitsquare.holidayserver.dto.HolidayResponse;
import com.planitsquare.holidayserver.dto.HolidaySearchCondition;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HolidayService {
    Page<HolidayResponse> searchHolidays(@Valid HolidaySearchCondition condition, Pageable pageable);

    void removeHoliday(@Valid HolidayRemoveReq request);
}
