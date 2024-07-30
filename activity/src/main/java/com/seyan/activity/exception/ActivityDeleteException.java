package com.seyan.activity.exception;

import java.io.Serial;

public class ActivityDeleteException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 2;

    public ActivityDeleteException(String message) {
        super(message);
    }
}
