package com.seyan.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CommentCreationDTO(
        @NotNull(message = "Content should not be null")
        String content,
        @NotNull(message = "User id should not be null")
        Long userId,
        @NotNull(message = "Post id should not be null")
        Long postId,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @NotNull(message = "Comment date should not be null")
        LocalDateTime commentDate
) {
}
