package com.seyan.film.exception.film;

import java.io.Serial;

public class IncorrectDateRangeException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 7;

    public IncorrectDateRangeException(String message) {
        super(message);
    }
}
