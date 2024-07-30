package com.seyan.film.film.dto;

import com.seyan.reviewmonolith.film.Genre;
import com.seyan.reviewmonolith.profile.Profile;

import java.time.LocalDate;
import java.util.List;

public record FilmUpdateDTO(
        //@NotNull(message = "Film id is required")
        //Long id,
        String title,
        String description,
        LocalDate releaseDate,
        Profile director,
        List<Profile> cast,

        //List<Long> castProfiles,
        //List<Genre> genres,
        Genre genre,
        Integer runningTimeMinutes
) {

}
