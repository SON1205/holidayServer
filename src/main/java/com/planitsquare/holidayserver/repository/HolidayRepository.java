package com.planitsquare.holidayserver.repository;

import com.planitsquare.holidayserver.domain.Country;
import com.planitsquare.holidayserver.domain.Holiday;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayRepository extends JpaRepository<Holiday, Long>, CustomHolidayRepository {
    List<Holiday> findAllByCountryAndDateBetween(Country country, LocalDate dateAfter, LocalDate dateBefore);

    void removeHolidayByCountryAndDateBetween(Country country, LocalDate dateAfter, LocalDate dateBefore);
}
