package com.apischanskyi.blackjack.exceptions;

public class BlackJackException extends RuntimeException {

    private BlackJackExceptionHelper.ErrorCode errorCode;

    BlackJackException(String message) {
        super(message);
    }

    BlackJackException(Throwable cause) {
        super(cause);
    }

    BlackJackException(String message, Throwable cause) {
        super(message, cause);
    }

    BlackJackException(BlackJackExceptionHelper.ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BlackJackExceptionHelper.ErrorCode getErrorCode() {
        return errorCode;
    }
}
