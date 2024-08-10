package com.seyan.list.dto;


import com.seyan.list.list.Privacy;

import java.util.List;

public record ListUpdateDTO(
        String title,
        String description,
        Privacy privacy,
        List<Long> filmIds
) {
}
