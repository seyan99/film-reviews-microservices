package com.seyan.film.dto.profile;

import java.util.List;

public record ProfileUpdateDTO(
        String name,
        String biography,
        List<Long> starringFilmsId,
        List<Long> directedFilmsId
) {
}
