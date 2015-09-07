package com.apischanskyi.blackjack.exceptions;

import org.springframework.http.HttpStatus;

public class BlackJackExceptionHelper {

    public static BlackJackException newBlackJackException(ErrorCode errorCode) {
        return new BlackJackException(errorCode);
    }

    public enum ErrorCode {
        YOU_ALREADY_HAVE_BET("You already have bet.", HttpStatus.BAD_REQUEST),
        DEAL_HAS_BEEN_PERFORMED("Deal already has been performed.", HttpStatus.BAD_REQUEST),
        HIT_IS_NOT_ALLOWED_HERE("Heat is not allowed if game not in IN-PROCESS state.", HttpStatus.BAD_REQUEST),
        USER_NOT_FOUND("User not found.", HttpStatus.NOT_FOUND),
        GAME_NOT_FOUND("Game not found.", HttpStatus.NOT_FOUND),
        INCOMPATIBLE_GAME_STATE("Game is in incompatible state.", HttpStatus.INTERNAL_SERVER_ERROR),
        NO_BET_FOUND("No bet found in request.", HttpStatus.BAD_REQUEST),
        INSUFFICIENT_BALANCE("You have insufficient amount of balance.", HttpStatus.PAYMENT_REQUIRED),
        UNABLE_TO_CANCEL_BET("Unable to cancel bet.", HttpStatus.BAD_REQUEST);

        String message;
        HttpStatus httpStatus;

        ErrorCode(String message, HttpStatus httpStatus) {
            this.message = message;
            this.httpStatus = httpStatus;
        }

        public String getMessage() {
            return message;
        }

        public HttpStatus getHttpStatus() {
            return httpStatus;
        }
    }

}
