package com.seyan.film.dto.film;

import com.seyan.film.film.Genre;
import com.seyan.film.profile.Profile;

import java.time.LocalDate;
import java.util.List;

public record FilmUpdateDTO(
        String title,
        String description,
        LocalDate releaseDate,
        Profile director,
        List<Profile> cast,
        Genre genre,
        Integer runningTimeMinutes
) {
}
