package com.seyan.list.exception;

import java.io.Serial;

public class SortingParametersException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 2;

    public SortingParametersException(String message) {
        super(message);
    }
}
