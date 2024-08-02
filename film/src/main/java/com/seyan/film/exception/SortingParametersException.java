package com.seyan.film.exception;

import java.io.Serial;

public class SortingParametersException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 3;

    public SortingParametersException(String message) {
        super(message);
    }
}
