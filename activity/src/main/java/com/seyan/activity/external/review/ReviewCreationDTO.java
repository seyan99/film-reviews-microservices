package com.seyan.activity.external.review;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReviewCreationDTO(
        Double rating,
        Boolean isLiked,
        String content,
        Boolean containsSpoilers,
        @NotNull(message = "Film id cannot be null")
        Long filmId,
        @NotNull(message = "User id cannot be null")
        Long userId,
        LocalDate watchedOnDate,
        Boolean watchedThisFilmBefore
) {
}
