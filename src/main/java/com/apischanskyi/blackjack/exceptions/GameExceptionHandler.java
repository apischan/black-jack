package com.apischanskyi.blackjack.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public final class GameExceptionHandler {

    @ExceptionHandler(BlackJackException.class)
    public ResponseEntity<ErrorInfo> handleGameException(BlackJackException ex) {
        BlackJackExceptionHelper.ErrorCode errorCode = ex.getErrorCode();
        ErrorInfo errorInfo = new ErrorInfo(errorCode.getHttpStatus(), errorCode.getMessage());
        return createResponseEntity(errorInfo);
    }

    protected ResponseEntity<ErrorInfo> createResponseEntity(ErrorInfo errorInfo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(errorInfo, headers, errorInfo.getHttpStatus());
    }


}
