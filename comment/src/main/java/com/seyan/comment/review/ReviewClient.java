package com.seyan.comment.review;


import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(
        name = "review-service",
        url = "${application.config.review-url}"
)
public interface ReviewClient {

    @PutMapping("/add-comment")
    void addReviewComment(@RequestParam("reviewId") Long reviewId, @RequestParam("commentId") Long commentId);

    @DeleteMapping("/delete-comment")
    void deleteReviewComment(@RequestParam("reviewId") Long reviewId, @RequestParam("commentId") Long commentId);
}
