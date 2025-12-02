package com.planitsquare.holidayserver.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
@Builder
public class ExceptionResponse {
    private final String code;
    private final String message;

    // 에러가 없으면 JSON에서 생략 (@JsonInclude)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ValidationError> errors;

    @Getter
    @Builder
    public static class ValidationError {
        private String field;
        private String message;
        private String value;

        public static ValidationError of(FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .value(String.valueOf(fieldError.getRejectedValue()))
                    .build();
        }
    }
}
