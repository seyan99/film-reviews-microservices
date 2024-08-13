package com.seyan.activity.dto;

import com.seyan.activity.activity.ActivityId;

import java.time.LocalDate;

public record ActivityResponseDTO(
        ActivityId id,
        Boolean isWatched,
        Boolean isLiked,
        Boolean isInWatchlist,
        Double rating,
        LocalDate watchlistAddDate,
        Boolean hasReview
) {
}
