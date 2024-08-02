package com.seyan.film.exception;

import java.io.Serial;

public class FilmNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 2;

    public FilmNotFoundException(String message) {
        super(message);
    }
}
