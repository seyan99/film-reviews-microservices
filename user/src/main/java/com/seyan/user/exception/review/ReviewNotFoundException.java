package com.seyan.user.exception.review;

import java.io.Serial;

public class ReviewNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 3;

    public ReviewNotFoundException(String message) {
        super(message);
    }
}
