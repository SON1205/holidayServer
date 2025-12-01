package com.planitsquare.holidayserver.dto;

import java.time.LocalDate;
import java.util.List;

public record HolidayApiRes(
        LocalDate date,
        String localName,
        String name,
        String countryCode,
        Boolean fixed,
        Boolean global,
        List<String> counties,
        Integer launchYear,
        List<String> types
) {
}
