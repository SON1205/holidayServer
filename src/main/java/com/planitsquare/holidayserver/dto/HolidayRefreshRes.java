package com.planitsquare.holidayserver.dto;

public record HolidayRefreshRes(
        String countryCode,
        int year,
        boolean refreshed,
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
