package com.seyan.film.dto.profile;

import java.util.List;

public record ProfileResponseDTO(
        Long id,
        String name,
        String biography,
        List<FilmInProfileResponseDTO> starringFilms,
        List<FilmInProfileResponseDTO> directedFilms,
        String url
) {
}
