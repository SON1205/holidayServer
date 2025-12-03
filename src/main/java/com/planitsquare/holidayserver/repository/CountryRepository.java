package com.planitsquare.holidayserver.repository;

import com.planitsquare.holidayserver.domain.Country;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByCountryCode(String countryCode);
}
