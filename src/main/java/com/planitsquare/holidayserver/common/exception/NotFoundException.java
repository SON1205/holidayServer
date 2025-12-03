package com.planitsquare.holidayserver.common.exception;

public class NotFoundException extends BaseException {
    public NotFoundException(String code, String message) {
        super(code, message);
    }

    public NotFoundException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
