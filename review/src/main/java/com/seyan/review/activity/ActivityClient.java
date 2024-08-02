package com.seyan.review.activity;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "activity-service",
        url = "${application.config.activity-url}"
)
public interface ActivityClient {

    @PatchMapping("/update-has-review")
    void updateHasReview(@RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId);
}
