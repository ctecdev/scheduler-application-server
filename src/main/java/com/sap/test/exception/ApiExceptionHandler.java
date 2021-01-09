package com.sap.test.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Log4j2
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleException(IllegalArgumentException e) {
        log.error(e.getClass().getSimpleName() + " has occurred", e);
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Throwable.class})
    public ResponseEntity<Object> handleException(Throwable t) {
        log.error(t.getClass().getSimpleName() + " has occurred", t);
        ErrorResponse errorResponse = new ErrorResponse(
                t.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}