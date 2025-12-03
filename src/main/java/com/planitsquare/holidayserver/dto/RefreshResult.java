package com.planitsquare.holidayserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "공휴일 재동기화 응답 DTO")
public record RefreshResult(
        @Schema(description = "나라코드(ISO 3166-1 alpha-2)", example = "KR")
        String countryCode,

        @Schema(description = "연도 (1900 ~ 2100)", example = "2025")
        int year,

        @Schema(description = "재동기화된 공휴일 전체 개수", example = "11")
        long total,

        @Schema(description = "새로 동기화된 공휴일 개수", example = "7")
        long inserted,

        @Schema(description = "재동기화된 공휴일 개수", example = "4")
        long updated
) {
    public static RefreshResult of(String countryCode, int year, long inserted, long updated) {
        return new RefreshResult(countryCode, year, inserted + updated, inserted, updated);
    }
}
