package com.qo_op.api.dto;

import lombok.Getter;

@Getter
public class ExceptionDto {
    private final String message;

    public ExceptionDto(String message) {
        this.message = message;
    }
}
