package com.seyan.film.profile.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProfileCreationDTO(
        @NotNull(message = "Profile name is required")
        String name,
        @NotNull(message = "Profile biography is required")
        String biography,
        List<Long> starringFilmsId,
        List<Long> directedFilmsId
) {

}
