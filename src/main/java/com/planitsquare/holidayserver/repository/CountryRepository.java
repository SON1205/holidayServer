package com.planitsquare.holidayserver.repository;

import com.planitsquare.holidayserver.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
