package com.seyan.film.exception.film;

import java.io.Serial;

public class ActivityDeleteException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 10;

    public ActivityDeleteException(String message) {
        super(message);
    }
}
