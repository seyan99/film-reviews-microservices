package com.seyan.review.exception.comment;

import java.io.Serial;

public class CommentNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 11;

    public CommentNotFoundException(String message) {
        super(message);
    }
}
