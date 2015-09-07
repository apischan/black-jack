package com.apischanskyi.blackjack.exceptions;

import org.springframework.http.HttpStatus;

public class ErrorInfo {
    private int httpStatusVal;
    private HttpStatus httpStatus;
    private String message;

    public ErrorInfo(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.httpStatusVal = httpStatus.value();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getHttpStatusVal() {
        return httpStatusVal;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
