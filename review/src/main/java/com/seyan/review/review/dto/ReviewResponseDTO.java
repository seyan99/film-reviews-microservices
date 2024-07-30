package com.seyan.review.review.dto;

import java.time.LocalDate;

public record ReviewResponseDTO(
        Long id,
        Double rating,
        Boolean isLiked,
        String content,
        LocalDate watchedOnDate,
        Boolean containsSpoilers,
        LocalDate creationDate,
        Long filmId,
        Long authorId,
        int reviewLikeCount,
        int commentCount
        //Boolean watchedThisFilmBefore
) {

}
