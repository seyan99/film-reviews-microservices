package com.seyan.activity.external.film;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "film-service",
        url = "${application.config.film-url}"
)
public interface FilmClient {
    @PutMapping("/{id}/update-like-count")
    void updateLikeCount(@RequestParam("id") Long id, @RequestParam Boolean toAdd);

    @PutMapping("/update-avg-rating")
    void updateAvgRating(@RequestParam("id") Long id, @RequestParam Double rating);

    @PutMapping("/update-watched-count")
    void updateWatchedCount(@RequestParam("id") Long id, @RequestParam Boolean toAdd);
}
