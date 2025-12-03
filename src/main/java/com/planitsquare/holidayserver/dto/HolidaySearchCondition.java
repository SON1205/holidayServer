package com.planitsquare.holidayserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Schema(description = "공휴일 검색 필터")
public record HolidaySearchCondition(
        @Schema(description = "나라코드(ISO 3166-1 alpha-2)", example = "KR")
        @Size(min = 2, max = 3)
        String countryCode,

        @Schema(description = "연도 (1900 ~ 2100)", example = "2025")
        @Min(value = 1900)
        @Max(value = 2100)
        Integer year,

        @Schema(description = "기간 검색 - 시작날짜", example = "2025-01-01")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate startDate,

        @Schema(description = "기간 검색 - 마지막날짜", example = "2025-05-31")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate endDate,

        @Schema(description = "공휴일 타입(Public, Bank, School, Authorities, Optional, Observance", example = "Public")
        String type
) {
    public HolidaySearchCondition {
        if (countryCode != null) {
            countryCode = countryCode.toUpperCase();
        }
    }

    @Schema(hidden = true)
    @AssertTrue(message = "시작일은 종료일보다 이전이어야 합니다.")
    public boolean isDateRangeValid() {
        if (startDate == null || endDate == null) {
            return true; // 둘 중 하나라도 없으면 검증 패스 (Not Null 체크는 별도)
        }
        return !startDate.isAfter(endDate);
    }

    @Schema(hidden = true)
    @AssertTrue(message = "입력한 연도와 날짜 범위의 연도가 일치하지 않습니다.")
    public boolean isYearMatchingDateRange() {
        if (year == null || (startDate == null && endDate == null)) {
            return true;
        }

        if (startDate != null && startDate.getYear() > year) {
            return false;
        }

        if (endDate != null && endDate.getYear() < year) {
            return false;
        }

        return true;
    }
}