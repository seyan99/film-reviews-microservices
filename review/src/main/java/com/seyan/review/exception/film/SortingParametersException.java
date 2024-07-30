package com.seyan.review.exception.film;

import java.io.Serial;

public class SortingParametersException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 6;

    public SortingParametersException(String message) {
        super(message);
    }
}
