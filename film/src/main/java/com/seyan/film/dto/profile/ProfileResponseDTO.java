package com.seyan.film.dto.profile;

import com.seyan.film.dto.film.FilmPreviewResponseDTO;

import java.util.List;

public record ProfileResponseDTO(
        Long id,
        String name,
        String biography,
        List<FilmPreviewResponseDTO> starringFilms,
        List<FilmPreviewResponseDTO> directedFilms,
        String url
) {
}
