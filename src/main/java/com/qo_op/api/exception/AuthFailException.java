package com.qo_op.api.exception;

import lombok.Getter;

@Getter
public class AuthFailException extends RuntimeException {
    private final String message;

    public AuthFailException(String message) {
        this.message = message;
    }
}
