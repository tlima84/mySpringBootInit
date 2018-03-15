package com.example.api.resources.infraestructure;

import com.example.api.core.exception.SystemException;
import com.example.api.core.exception.UserException;
import com.example.api.resources.common.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final String DEFAULT_ERROR_MESSAGE = "An unhandled error ocurred";
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);
    private static final String ERROR_MESSAGE = "An user error occurred";

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorResponse> exceptionHandleGenericoRunTime(RuntimeException ex, WebRequest request){
        LOGGER.error(DEFAULT_ERROR_MESSAGE, ex);
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.toString()).description(ex.getMessage()).error(ex.getMessage()).build());
    }

    @ExceptionHandler(value = SystemException.class)
    public ResponseEntity<ErrorResponse> systemExceptionHandler(SystemException ex, WebRequest request) {
        LOGGER.error(DEFAULT_ERROR_MESSAGE, ex);

        return ResponseEntity.status(ex.getErrorCode())
                .body(ErrorResponse.builder()
                        .code(ex.getErrorCode().toString())
                        .description(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(value = UserException.class)
    public ResponseEntity<ErrorResponse> userExceptionHandler(UserException ex, WebRequest request) {
        LOGGER.error(ERROR_MESSAGE, ex);

        return ResponseEntity.status(ex.getErrorCode())
                .body(ErrorResponse.builder()
                        .code(ex.getErrorCode().toString())
                        .description(ex.getMessage())
                        .error(ex.getMessage())
                        .build());
    }

}
