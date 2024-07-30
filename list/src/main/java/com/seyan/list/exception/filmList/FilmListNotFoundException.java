package com.seyan.list.exception.filmList;

import java.io.Serial;

public class FilmListNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5;

    public FilmListNotFoundException(String message) {
        super(message);
    }
}
