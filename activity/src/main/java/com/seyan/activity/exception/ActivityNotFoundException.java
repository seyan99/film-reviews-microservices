package com.seyan.activity.exception;

import java.io.Serial;

public class ActivityNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1;

    public ActivityNotFoundException(String message) {
        super(message);
    }
}
