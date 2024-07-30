package com.seyan.review.exception.film;

import java.io.Serial;

public class FilmNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 8;

    public FilmNotFoundException(String message) {
        super(message);
    }
}
