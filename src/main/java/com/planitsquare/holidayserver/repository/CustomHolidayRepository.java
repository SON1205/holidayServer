package com.planitsquare.holidayserver.repository;

import com.planitsquare.holidayserver.domain.Holiday;
import com.planitsquare.holidayserver.dto.HolidaySearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomHolidayRepository {
    Page<Holiday> search(HolidaySearchCondition condition, Pageable pageable);
}
