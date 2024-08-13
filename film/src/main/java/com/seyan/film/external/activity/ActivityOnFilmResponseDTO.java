package com.seyan.film.external.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
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
