package com.seyan.list.exception.user;

import java.io.Serial;

public class EmailAlreadyExistsException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 2;

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
