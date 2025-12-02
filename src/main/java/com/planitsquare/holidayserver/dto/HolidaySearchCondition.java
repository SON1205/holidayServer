package com.planitsquare.holidayserver.dto;

import jakarta.validation.constraints.AssertTrue;
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
    public HolidaySearchCondition {
        if (countryCode != null) {
            countryCode = countryCode.toUpperCase();
        }
    }

    @AssertTrue(message = "시작일은 종료일보다 이전이어야 합니다.")
    public boolean isDateRangeValid() {
        if (startDate == null || endDate == null) {
            return true; // 둘 중 하나라도 없으면 검증 패스 (Not Null 체크는 별도)
        }
        return !startDate.isAfter(endDate);
    }

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