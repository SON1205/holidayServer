package com.planitsquare.holidayserver.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record HolidayRefreshReq(
        @Size(min = 2, max = 3)
        String countryCode,

        @Min(1900) @Max(2100)
        Integer year
) {
}
