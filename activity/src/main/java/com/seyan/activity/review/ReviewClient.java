package com.seyan.activity.review;

import com.seyan.activity.responsewrapper.CustomResponseWrapper;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(
        name = "review-service",
        url = "${application.config.review-url}"
)
public interface ReviewClient {
    @PostMapping("/create")
    void createReview(@RequestBody @Valid ReviewCreationDTO dto);

    @GetMapping("/count-reviews")
    Optional<CustomResponseWrapper<Long>> countUserReviewsForFilm(@RequestParam Long userId, @RequestParam Long filmId);
}
