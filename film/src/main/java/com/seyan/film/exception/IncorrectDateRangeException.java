package com.seyan.film.exception;

import java.io.Serial;

public class IncorrectDateRangeException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 4;

    public IncorrectDateRangeException(String message) {
        super(message);
    }
}
