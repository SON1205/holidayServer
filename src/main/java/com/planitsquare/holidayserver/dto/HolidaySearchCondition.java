package com.planitsquare.holidayserver.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record HolidaySearchCondition(
        @Size(min = 2, max = 3)
        String countryCode,

        @Min(value = 1900)
        @Max(value = 2100)
        Integer year,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate startDate,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate endDate,

        String type
) {
}
