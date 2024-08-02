package com.seyan.comment.exception;

import java.io.Serial;

public class CommentNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1;

    public CommentNotFoundException(String message) {
        super(message);
    }
}
