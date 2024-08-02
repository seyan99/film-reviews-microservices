package com.seyan.activity.dto;

import com.seyan.activity.activity.ActivityOnFilmId;

import java.time.LocalDate;

public record ActivityOnFilmResponseDTO(
        ActivityOnFilmId id,
        Boolean isWatched,
        Boolean isLiked,
        Boolean isInWatchlist,
        Double rating,
        LocalDate watchlistAddDate,
        Boolean hasReview
) {
}
