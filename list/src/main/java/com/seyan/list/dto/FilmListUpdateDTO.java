package com.seyan.list.dto;


import com.seyan.list.filmlist.Privacy;

import java.util.List;

public record FilmListUpdateDTO(
        String title,
        String description,
        Privacy privacy,
        List<Long> filmIds
) {
}
