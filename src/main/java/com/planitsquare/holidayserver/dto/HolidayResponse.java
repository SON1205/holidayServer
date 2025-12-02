package com.planitsquare.holidayserver.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.planitsquare.holidayserver.domain.Holiday;
import java.time.LocalDate;

public record HolidayResponse(
        Long id,
        String countryCode,
        String name,
        String localName,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date
) {
    public static HolidayResponse from(Holiday holiday) {
        return new HolidayResponse(
                holiday.getId(),
                holiday.getCountry().getCountryCode(),
                holiday.getName(),
                holiday.getLocalName(),
                holiday.getDate()
        );
    }
}
