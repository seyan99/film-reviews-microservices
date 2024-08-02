package com.seyan.film.dto.film;

import com.seyan.film.film.Genre;

import java.time.LocalDate;
import java.util.List;

public record FilmResponseDTO(
        Long id,
        String title,
        String description,
        LocalDate releaseDate,
        String filmUrl,
        Double rating,
        ProfileInFilmResponseDTO director,
        List<ProfileInFilmResponseDTO> cast,
        Genre genre,
        Integer runningTimeMinutes,
        Long watchedCount,
        Long listCount,
        Long likeCount
) {

}
