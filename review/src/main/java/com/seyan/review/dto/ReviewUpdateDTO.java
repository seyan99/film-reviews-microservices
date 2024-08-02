package com.seyan.review.dto;

import java.time.LocalDate;

public record ReviewUpdateDTO(
        //@NotNull
        //Long id,
        Double rating,
        Boolean isLiked,
        String content,
        Boolean containsSpoilers,
        LocalDate watchedOnDate,
        Boolean watchedThisFilmBefore
) {

}
