package com.planitsquare.holidayserver.common.exception;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        return createValidationResponse(e.getBindingResult());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionResponse> handleBindException(BindException e) {
        return createValidationResponse(e.getBindingResult());
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ExceptionResponse> handleBaseException(BaseException e) {
        log.error("errorMessage: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .code(e.getCode())
                        .message(e.getMessage())
                        .build());
    }

    // --- 공통 검증 에러 생성 메서드 ---
    private ResponseEntity<ExceptionResponse> createValidationResponse(BindingResult bindingResult) {
        log.error("errorMessage: {}", bindingResult.getFieldError().getDefaultMessage());
        List<ExceptionResponse.ValidationError> errors = bindingResult.getFieldErrors().stream()
                .map(ExceptionResponse.ValidationError::of)
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .code("INVALID_INPUT")
                        .message("입력값이 올바르지 않습니다.")
                        .errors(errors)
                        .build());
    }
}