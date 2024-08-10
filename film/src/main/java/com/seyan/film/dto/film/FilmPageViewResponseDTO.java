package com.seyan.film.dto.film;

import com.seyan.film.external.activity.ActivityOnFilmResponseDTO;
import com.seyan.film.external.review.ReviewResponseDTO;

import java.util.List;
import java.util.Map;

public record FilmPageViewResponseDTO(
        FilmResponseDTO film,
        ActivityOnFilmResponseDTO activity,
        Map<String, List<ReviewResponseDTO>> reviews
) {
}
