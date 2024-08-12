package com.seyan.review.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReviewResponseDTO(
        Long id,
        Double rating,
        Boolean isLiked,
        String content,
        LocalDate watchedOnDate,
        Boolean containsSpoilers,
        LocalDateTime creationDate,
        Long filmId,
        String title,
        Long userId,
        String username,
        long reviewLikeCount,
        long commentCount
) {
}
