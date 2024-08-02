package com.seyan.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CommentCreationDTO(
        @NotNull(message = "Content cannot be null")
        String content,
        @NotNull(message = "User cannot not be null")
        Long userId,
        @NotNull(message = "Post id cannot be null")
        Long postId,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @NotNull(message = "Comment date cannot be null")
        LocalDateTime commentDate
) {
}
