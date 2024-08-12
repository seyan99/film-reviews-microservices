package com.seyan.list.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seyan.list.list.Privacy;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record ListCreationDTO(
        @NotNull(message = "User id cannot be null")
        Long userId,
        @NotNull(message = "Username cannot be null")
        String username,
        @NotNull(message = "Please enter the list name")
        String title,
        String description,
        @NotNull(message = "Privacy cannot be null")
        Privacy privacy,
        @NotNull(message = "A list must include at least one film")
        List<Long> filmIds,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @NotNull(message = "Creation date cannot be null")
        LocalDateTime creationDate
) {
}
