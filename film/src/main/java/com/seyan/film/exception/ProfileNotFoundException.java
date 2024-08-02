package com.seyan.film.exception;

import java.io.Serial;

public class ProfileNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1;

    public ProfileNotFoundException(String message) {
        super(message);
    }
}
