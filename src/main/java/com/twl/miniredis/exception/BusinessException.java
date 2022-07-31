package com.twl.miniredis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BusinessException extends Exception {

    public BusinessException() {}

    public BusinessException(String s) {
        super(s);
    }

    public BusinessException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public BusinessException(Throwable throwable) {
        super(throwable);
    }

    public BusinessException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
