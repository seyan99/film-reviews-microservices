package com.seyan.activity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ActivityAndReviewCreationDTO(
        @NotNull(message = "Film id cannot be null")
        Long filmId,
        @NotNull(message = "User id cannot be null")
        Long userId,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate watchedOnDate,
        Boolean watchedThisFilmBefore,
        String reviewContent,
        @Max(value = 5, message = "Maximum value 5.0")
        Double rating,
        Boolean isLiked,
        Boolean containsSpoilers
) {
}
