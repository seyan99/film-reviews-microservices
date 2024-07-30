package com.seyan.list.exception.profile;

import java.io.Serial;

public class ProfileNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 4;

    public ProfileNotFoundException(String message) {
        super(message);
    }
}
