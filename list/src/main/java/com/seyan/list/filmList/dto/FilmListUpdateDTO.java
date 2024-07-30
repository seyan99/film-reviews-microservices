package com.seyan.list.filmList.dto;

import com.seyan.reviewmonolith.filmList.Privacy;

import java.util.List;

public record FilmListUpdateDTO(
        String title,
        String description,
        Privacy privacy,
        List<Long> filmIds
) {
}
