package com.planitsquare.holidayserver.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

    ;

    private final String code;
    private final String message;
}
