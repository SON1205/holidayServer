package com.planitsquare.holidayserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "공휴일 삭제 요청 DTO")
public record HolidayRemoveReq(
        @Schema(description = "나라코드(ISO 3166-1 alpha-2)", example = "KR")
        @Size(min = 2, max = 3)
        @NotNull
        String countryCode,

        @Schema(description = "연도 (1900 ~ 2100)", example = "2025")
        @Min(1900) @Max(2100)
        @NotNull
        Integer year
) {
}
