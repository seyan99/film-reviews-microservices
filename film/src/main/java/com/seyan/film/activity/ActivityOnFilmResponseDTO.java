package com.seyan.film.activity;

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
