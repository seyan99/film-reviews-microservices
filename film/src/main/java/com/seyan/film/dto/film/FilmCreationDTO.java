package com.seyan.film.dto.film;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seyan.film.film.Genre;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record FilmCreationDTO(
        @NotNull(message = "Film title is required")
        String title,
        @NotNull(message = "Film description is required")
        String description,
        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull(message = "Film release date is required")
        LocalDate releaseDate,
        //@NotNull(message = "Film director is required")
        //Profile director,
        //@NotNull(message = "Film cast profiles is required")
        Long directorId,
        //todo long list
        List<Long> castIdList,
        //List<Profile> cast,
        //List<Long> castProfiles,
        /*@NotNull(message = "Film genres is required")
        List<Genre> genres,*/
        @NotNull(message = "Film genre is required")
        Genre genre,
        @NotNull(message = "Film running time is required")
        Integer runningTimeMinutes
) {

}
