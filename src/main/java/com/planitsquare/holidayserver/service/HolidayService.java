package com.planitsquare.holidayserver.service;

import com.planitsquare.holidayserver.domain.Country;
import com.planitsquare.holidayserver.domain.Holiday;
import com.planitsquare.holidayserver.dto.HolidayApiRes;
import java.util.List;

public interface HolidayService {
    List<Holiday> saveHolidays(Country c, List<HolidayApiRes> holidays);
}
