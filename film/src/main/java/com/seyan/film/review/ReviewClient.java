package com.seyan.film.review;


import com.seyan.film.responsewrapper.CustomResponseWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@FeignClient(
        name = "review-service",
        url = "${application.config.review-url}"
)
public interface ReviewClient {

    @GetMapping("/get-film-ids")
    CustomResponseWrapper<List<Long>> getAllFilmIds();

    @GetMapping("/get-film-ids-based-on-review-date-after")
    CustomResponseWrapper<List<Long>> getFilmIdsBasedOnReviewDateAfter(@RequestParam("date") LocalDate date);

    @GetMapping("/latest-and-popular")
    CustomResponseWrapper<Map<String, List<ReviewResponseDTO>>> latestAndPopularReviewsForFilm(@RequestParam Long filmId);
}
