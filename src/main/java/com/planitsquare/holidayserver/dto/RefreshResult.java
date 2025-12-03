package com.planitsquare.holidayserver.dto;

public record RefreshResult(
        String countryCode,
        int year,
        long total,
        long inserted,
        long updated
) {
    public static RefreshResult of(String countryCode, int year, long inserted, long updated) {
        return new RefreshResult(countryCode, year, inserted + updated, inserted, updated);
    }
}
