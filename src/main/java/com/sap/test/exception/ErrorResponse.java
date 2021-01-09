package com.sap.test.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ErrorResponse {

    private final String message;
    private final HttpStatus httpStatus;
    private final String timestamp;

    public ErrorResponse(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = createTimestamp();
    }

    private String createTimestamp() {
        String timeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat);
        return LocalDateTime.now().format(formatter);
    }

}
