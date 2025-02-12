package com.sofkau.auth.application.exceptions.token;

import static com.sofkau.auth.constants.TokenMessageConstants.TOKEN_NOT_VALID;

public class NotValidTokenFoundException extends RuntimeException {
    public NotValidTokenFoundException() {
        super(TOKEN_NOT_VALID);
    }
}
