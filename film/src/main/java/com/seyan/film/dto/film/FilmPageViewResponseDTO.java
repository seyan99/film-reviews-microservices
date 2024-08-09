package com.seyan.film.dto.film;

import com.seyan.film.activity.ActivityOnFilmResponseDTO;
import com.seyan.film.review.ReviewResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

public record FilmPageViewResponseDTO(
        FilmResponseDTO film,
        ActivityOnFilmResponseDTO activity,
        Map<String, List<ReviewResponseDTO>> reviews
) {
}
