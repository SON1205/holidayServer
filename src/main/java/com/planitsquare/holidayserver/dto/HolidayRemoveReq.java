package com.planitsquare.holidayserver.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record HolidayRemoveReq(
        @Size(min = 2, max = 3)
        @NotNull
        String countryCode,

        @Min(1900) @Max(2100)
        @NotNull
        Integer year
) {
}
