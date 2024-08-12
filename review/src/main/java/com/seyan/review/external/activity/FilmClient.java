package com.seyan.review.external.activity;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "film-service",
        url = "${application.config.film-url}"
)
public interface FilmClient {

    @PutMapping("/update-review-count")
    void updateReviewCount(@RequestParam("id") Long filmId, @RequestParam("add") boolean add);
}
