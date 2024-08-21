package com.seyan.activity.external.review;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReviewCreationDTO(
        Double rating,
        Boolean isLiked,
        String content,
        Boolean containsSpoilers,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @NotNull(message = "Creation date cannot be null")
        LocalDateTime creationDate,
        @NotNull(message = "Film id cannot be null")
        Long filmId,
        String title,
        @NotNull(message = "User id cannot be null")
        Long userId,
        String username,
        LocalDate watchedOnDate,
        Boolean watchedThisFilmBefore
) {
}
