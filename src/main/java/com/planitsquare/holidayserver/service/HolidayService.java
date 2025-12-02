package com.planitsquare.holidayserver.service;

import com.planitsquare.holidayserver.domain.Country;
import com.planitsquare.holidayserver.domain.Holiday;
import com.planitsquare.holidayserver.dto.HolidayApiRes;
import com.planitsquare.holidayserver.dto.HolidayResponse;
import com.planitsquare.holidayserver.dto.HolidaySearchCondition;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HolidayService {
    List<Holiday> saveHolidays(Country c, List<HolidayApiRes> holidays);

    Page<HolidayResponse> searchHolidays(@Valid HolidaySearchCondition condition, Pageable pageable);
}
