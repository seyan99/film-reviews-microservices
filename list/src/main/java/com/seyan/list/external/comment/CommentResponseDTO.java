package com.seyan.list.external.comment;

import java.time.LocalDateTime;

public record CommentResponseDTO(
        Long id,
        String content,
        Long userId,
        LocalDateTime commentDate,
        Boolean isEdited
) {
}
