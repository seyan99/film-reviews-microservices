package com.seyan.review.external.activity;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "activity-service",
        url = "${application.config.activity-url}"
)
public interface ActivityClient {

    @PutMapping("/update-has-review")
    void updateHasReview(@RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId);
}
