package com.seyan.list.exception;

import java.io.Serial;

public class ListNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1;

    public ListNotFoundException(String message) {
        super(message);
    }
}
