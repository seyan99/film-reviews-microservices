package com.seyan.list.exception;

import java.io.Serial;

public class FilmListNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1;

    public FilmListNotFoundException(String message) {
        super(message);
    }
}
