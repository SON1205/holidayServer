package com.planitsquare.holidayserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "공휴일 재동기화 응답 DTO")
public record HolidayRefreshRes(
        @Schema(description = "나라코드(ISO 3166-1 alpha-2)", example = "KR")
        String countryCode,

        @Schema(description = "연도 (1900 ~ 2100)", example = "2025")
        int year,

        @Schema(description = "재동기화 여부", example = "true")
        boolean refreshed,

        @Schema(description = "재동기화된 공휴일 개수", example = "40")
        long total
) {
    public static HolidayRefreshRes from(RefreshResult refreshed) {
        return new HolidayRefreshRes(
                refreshed.countryCode(),
                refreshed.year(),
                refreshed.total() != 0,
                refreshed.total()
        );
    }
}
