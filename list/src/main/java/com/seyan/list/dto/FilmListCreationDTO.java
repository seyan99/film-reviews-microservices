package com.seyan.list.dto;

import com.seyan.list.filmlist.Privacy;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record FilmListCreationDTO(
        @NotNull(message = "User id cannot be null")
        Long userId,
        @NotNull(message = "Please enter the list name")
        String title,
        String description,
        @NotNull(message = "Privacy cannot be null")
        Privacy privacy,
        @NotNull(message = "A list must include at least one film")
        List<Long> filmIds
) {
}
