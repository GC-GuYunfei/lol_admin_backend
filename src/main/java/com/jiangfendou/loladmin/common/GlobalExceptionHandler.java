package com.jiangfendou.loladmin.common;

import com.jiangfendou.loladmin.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiResponse> handler(RuntimeException runtimeException) {
        log.error("运行时异常异常：-----------{}", runtimeException.getMessage());
        return new ResponseEntity<>(ApiResponse.failed(HttpStatus.INTERNAL_SERVER_ERROR,
            new ApiError(ErrorCode.SYSTEM_ERROR.getCode(),
                ErrorCode.SYSTEM_ERROR.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handler(IllegalArgumentException illegalArgumentException) {
        log.error("Assert异常：-----------{}", illegalArgumentException.getMessage());
        return new ResponseEntity<>(ApiResponse.failed(HttpStatus.INTERNAL_SERVER_ERROR,
            new ApiError(ErrorCode.SYSTEM_ERROR.getCode(),
                ErrorCode.SYSTEM_ERROR.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ApiResponse> handler(BusinessException businessException) {
        log.error("Business异常：-----------{}", businessException.apiError.getMessage());
        return new ResponseEntity<>(ApiResponse.failed(businessException.getHttpStatus(),
                new ApiError(businessException.getApiError().getCode(),
                    businessException.getApiError().getMessage())), businessException.getHttpStatus());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handler(AccessDeniedException accessDeniedException) {
        log.error("拒绝访问：-----------{}", ErrorCode.NO_ACCESS_ALLOWED_ERROR.getMessage());
        return new ResponseEntity<>(ApiResponse.failed(HttpStatus.UNAUTHORIZED,
            new ApiError(ErrorCode.NO_ACCESS_ALLOWED_ERROR.getCode(),
                ErrorCode.NO_ACCESS_ALLOWED_ERROR.getMessage())), HttpStatus.UNAUTHORIZED);
    }
}
