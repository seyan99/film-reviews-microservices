package com.seyan.activity.film;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "film-service",
        url = "${application.config.film-url}"
)
public interface FilmClient {
    @PatchMapping("/{id}/update-like")
    void updateLikeCount(@RequestParam("id") Long id, @RequestParam Boolean toAdd);

    @PatchMapping("/update-avg-rating")
    void updateAvgRating(@RequestParam("id") Long id, @RequestParam Double rating);

    @PatchMapping("/update-watched-count")
    void updateWatchedCount(@RequestParam("id") Long id, @RequestParam Boolean toAdd);
}
