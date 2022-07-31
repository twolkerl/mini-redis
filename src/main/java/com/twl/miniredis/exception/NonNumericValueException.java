package com.twl.miniredis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class NonNumericValueException extends Exception {

    public NonNumericValueException() {}

    public NonNumericValueException(String s) {
        super(s);
    }

    public NonNumericValueException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public NonNumericValueException(Throwable throwable) {
        super(throwable);
    }

    public NonNumericValueException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
