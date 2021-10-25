package com.qo_op.api.exception;

import lombok.Getter;

@Getter
public class LoginFailException extends RuntimeException {

    private final String message;

    public LoginFailException(String message) {
        this.message = message;
    }
}
