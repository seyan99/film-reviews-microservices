package com.seyan.review.review;


import com.seyan.review.dto.*;
import com.seyan.review.responsewrapper.CustomResponseWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
@RestController
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CustomResponseWrapper<ReviewResponseDTO>> createReview(@RequestBody @Valid ReviewCreationDTO dto) {
        Review review = reviewService.createReview(dto);
        ReviewResponseDTO response = reviewMapper.mapReviewToReviewResponseDTO(review);
        CustomResponseWrapper<ReviewResponseDTO> wrapper = CustomResponseWrapper.<ReviewResponseDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message("Review has been successfully created")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.CREATED);
    }

    @PutMapping("/add-comment")
    public ResponseEntity<CustomResponseWrapper<ReviewResponseDTO>> addReviewComment(@RequestParam("reviewId") Long reviewId, @RequestParam("commentId") Long commentId) {
        Review review = reviewService.addReviewComment(reviewId, commentId);
        ReviewResponseDTO response = reviewMapper.mapReviewToReviewResponseDTO(review);
        CustomResponseWrapper<ReviewResponseDTO> wrapper = CustomResponseWrapper.<ReviewResponseDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message("Comment has been added")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.CREATED);
    }

    @PatchMapping("/delete-comment")
    public ResponseEntity<CustomResponseWrapper<ReviewResponseDTO>> deleteReviewComment(@RequestParam("reviewId") Long reviewId, @RequestParam("commentId") Long commentId) {
        Review review = reviewService.deleteReviewComment(reviewId, commentId);
        ReviewResponseDTO response = reviewMapper.mapReviewToReviewResponseDTO(review);
        CustomResponseWrapper<ReviewResponseDTO> wrapper = CustomResponseWrapper.<ReviewResponseDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message("Comment has been deleted")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.CREATED);
    }

    @PatchMapping("/update-likes")
    public ResponseEntity<CustomResponseWrapper<ReviewResponseDTO>> updateReviewLikes(@RequestParam("reviewId") Long reviewId, @RequestParam("userId") Long userId) {
        Review review = reviewService.updateReviewLikes(reviewId, userId);
        ReviewResponseDTO response = reviewMapper.mapReviewToReviewResponseDTO(review);
        CustomResponseWrapper<ReviewResponseDTO> wrapper = CustomResponseWrapper.<ReviewResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Review likes has been updated")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<CustomResponseWrapper<ReviewResponseDTO>> updateReview(@PathVariable("id") Long reviewId, @RequestBody @Valid ReviewUpdateDTO dto) {
        Review review = reviewService.updateReview(reviewId, dto);
        ReviewResponseDTO response = reviewMapper.mapReviewToReviewResponseDTO(review);
        CustomResponseWrapper<ReviewResponseDTO> wrapper = CustomResponseWrapper.<ReviewResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Review has been updated")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<CustomResponseWrapper<ReviewResponseDTO>> deleteReview(@PathVariable("id") Long reviewId) {
        reviewService.deleteReview(reviewId);
        CustomResponseWrapper<ReviewResponseDTO> wrapper = CustomResponseWrapper.<ReviewResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Review has been deleted")
                .data(null)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<CustomResponseWrapper<ReviewResponseDTO>> reviewDetails(@PathVariable("id") Long reviewId) {
        Review review = reviewService.getReviewById(reviewId);
        ReviewResponseDTO response = reviewMapper.mapReviewToReviewResponseDTO(review);
        CustomResponseWrapper<ReviewResponseDTO> wrapper = CustomResponseWrapper.<ReviewResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Review details")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/get-film-ids")
    public ResponseEntity<CustomResponseWrapper<List<Long>>> getAllFilmIds() {
        List<Long> response = reviewService.getAllFilmIds();
        CustomResponseWrapper<List<Long>> wrapper = CustomResponseWrapper.<List<Long>>builder()
                .status(HttpStatus.OK.value())
                .message("All film ids from reviews DB")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/get-film-ids-based-on-review-date-after")
    public ResponseEntity<CustomResponseWrapper<List<Long>>> getFilmIdsBasedOnReviewDateAfter(@RequestParam("date") LocalDate date) {
        List<Long> response = reviewService.getFilmIdsBasedOnReviewDateAfter(date);
        CustomResponseWrapper<List<Long>> wrapper = CustomResponseWrapper.<List<Long>>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("All film ids based on reviews date after: %s", date))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/by-film")
    public ResponseEntity<CustomResponseWrapper<PageableReviewResponseDTO>> getAllReviewsByFilmId(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam Long filmId) {

        Page<Review> reviews = reviewService.getReviewsByFilmId(filmId, page, size);
        PageableReviewResponseDTO response = reviewMapper.mapReviewsPageToPageableReviewResponseDTO(reviews);
        CustomResponseWrapper<PageableReviewResponseDTO> wrapper = CustomResponseWrapper.<PageableReviewResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("List of reviews for film with ID: %s", filmId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/{userId}/all")
    public ResponseEntity<CustomResponseWrapper<PageableReviewResponseDTO>> getAllReviewsByUserId(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @PathVariable("userId") Long userId) {

        Page<Review> reviews = reviewService.getReviewsByUserId(userId, page, size);
        PageableReviewResponseDTO response = reviewMapper.mapReviewsPageToPageableReviewResponseDTO(reviews);
        CustomResponseWrapper<PageableReviewResponseDTO> wrapper = CustomResponseWrapper.<PageableReviewResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("List of reviews of user with ID: %s", userId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/films")
    public ResponseEntity<CustomResponseWrapper<PageableReviewResponseDTO>> getReviewsByUserIdAndFilmId(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId) {

        Page<Review> reviews = reviewService.getReviewsByUserIdAndFilmId(userId, filmId, page, size);
        PageableReviewResponseDTO response = reviewMapper.mapReviewsPageToPageableReviewResponseDTO(reviews);
        CustomResponseWrapper<PageableReviewResponseDTO> wrapper = CustomResponseWrapper.<PageableReviewResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("List of reviews for film with ID: %s of user with ID: %s", filmId, userId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/{userId}/films/diary")
    public ResponseEntity<CustomResponseWrapper<PageableReviewResponseDTO>> getDiary(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @PathVariable("userId") Long userId) {

        Page<Review> reviews = reviewService.getReviewsByUserIdAsDiary(userId, page, size);
        PageableReviewResponseDTO response = reviewMapper.mapReviewsPageToPageableReviewResponseDTO(reviews);
        CustomResponseWrapper<PageableReviewResponseDTO> wrapper = CustomResponseWrapper.<PageableReviewResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Diary films of user with ID: %s", userId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<CustomResponseWrapper<PageableReviewResponseDTO>> getAllReviews(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size) {

        Page<Review> reviews = reviewService.getAllReviews(page, size);
        PageableReviewResponseDTO response = reviewMapper.mapReviewsPageToPageableReviewResponseDTO(reviews);
        CustomResponseWrapper<PageableReviewResponseDTO> wrapper = CustomResponseWrapper.<PageableReviewResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("List of all reviews")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/count-reviews")
    public ResponseEntity<CustomResponseWrapper<Long>> countUserReviewsForFilm(@RequestParam Long userId, @RequestParam Long filmId) {
        int response = reviewService.countUserReviewsForFilm(userId, filmId);
        CustomResponseWrapper<Long> wrapper = CustomResponseWrapper.<Long>builder()
                .status(HttpStatus.OK.value())
                .message("List of all reviews")
                .data((long) response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/latest-and-popular")
    public ResponseEntity<CustomResponseWrapper<Map<String, List<ReviewResponseDTO>>>> latestAndPopularReviewsForFilm(@RequestParam Long filmId) {
        Map<String, List<Review>> reviews = reviewService.getLatestAndPopularReviewsForFilm(filmId);
        List<ReviewResponseDTO> latest = reviewMapper.mapReviewToReviewResponseDTO(reviews.get("latest"));
        List<ReviewResponseDTO> popular = reviewMapper.mapReviewToReviewResponseDTO(reviews.get("popular"));
        HashMap<String, List<ReviewResponseDTO>> response = new HashMap<>();
        response.put("latest", latest);
        response.put("popular", popular);

        CustomResponseWrapper<Map<String, List<ReviewResponseDTO>>> wrapper = CustomResponseWrapper.<Map<String, List<ReviewResponseDTO>>>builder()
                .status(HttpStatus.OK.value())
                .message("Latest and popular reviews by film")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }
}
