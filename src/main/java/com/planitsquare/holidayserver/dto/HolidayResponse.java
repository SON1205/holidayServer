package com.planitsquare.holidayserver.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.planitsquare.holidayserver.domain.Holiday;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "공휴일 응답 DTO")
public record HolidayResponse(
        @Schema(description = "id", example = "5510")
        Long id,

        @Schema(description = "나라코드(ISO 3166-1 alpha-2)", example = "KR")
        String countryCode,

        @Schema(description = "영어 이름", example = "New Year's Day")
        String name,

        @Schema(description = "로컬 이름", example = "새해")
        String localName,

        @Schema(description = "공휴일 날짜", example = "2025-01-01")
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
