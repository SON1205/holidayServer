package com.planitsquare.holidayserver.repository;

import com.planitsquare.holidayserver.domain.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayRepository extends JpaRepository<Holiday, Long>, CustomHolidayRepository {
}
