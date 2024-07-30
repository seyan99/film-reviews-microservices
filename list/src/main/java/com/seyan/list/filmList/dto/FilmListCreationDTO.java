package com.seyan.list.filmList.dto;

import com.seyan.reviewmonolith.filmList.Privacy;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record FilmListCreationDTO(
        @NotNull(message = "User id should not be null")
        Long userId,
        @NotNull(message = "Please enter the list name")
        String title,
        String description,
        @NotNull(message = "Privacy should not be null")
        Privacy privacy,
        @NotNull(message = "A list must include at least one film")
        List<Long> filmIds
) {

}
