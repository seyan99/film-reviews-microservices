package com.seyan.review.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReviewCreationDTO(
        //@NotNull
        Double rating,
        //@NotNull
        Boolean isLiked,
        //@NotNull
        String content,
        Boolean containsSpoilers,
        @NotNull(message = "Film id should not be null")
        Long filmId,
        @NotNull(message = "User id should not be null")
        Long userId,
        LocalDate watchedOnDate,
        Boolean watchedThisFilmBefore
) {

}
