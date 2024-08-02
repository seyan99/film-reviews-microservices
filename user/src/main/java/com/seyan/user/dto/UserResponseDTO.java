package com.seyan.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;

    private String username;

    List<Long> watchedFilms;

    List<Long> watchlistFilms;

    List<Long> likesFilms;

    List<Long> likesReviews;

    List<Long> likesLists;

    List<Long> followingUsers;

    List<Long> followersUsers;

    List<Long> blockedUsers;
}
