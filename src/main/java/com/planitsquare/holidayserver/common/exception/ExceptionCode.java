package com.planitsquare.holidayserver.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

    NOT_FOUND_COUNTRY_CODE("NOT_FOUND_COUNTRY_CODE", "해당 나라코드가 존재하지 않습니다."),
    ;

    private final String code;
    private final String message;
}
