package com.seyan.list.exception;

import java.io.Serial;

public class ListParametersException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 3;

    public ListParametersException(String message) {
        super(message);
    }
}
