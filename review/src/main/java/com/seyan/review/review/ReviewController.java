package com.seyan.review.review;


import com.seyan.review.dto.ReviewCreationDTO;
import com.seyan.review.dto.ReviewMapper;
import com.seyan.review.dto.ReviewResponseDTO;
import com.seyan.review.dto.ReviewUpdateDTO;
import com.seyan.review.responsewrapper.CustomResponseWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    @GetMapping("/{id}")
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
    public ResponseEntity<CustomResponseWrapper<List<ReviewResponseDTO>>> getAllReviewsByFilmId(@RequestParam Long filmId) {
        List<Review> reviews = reviewService.getReviewsByFilmId(filmId);
        List<ReviewResponseDTO> response = reviewMapper.mapReviewToReviewResponseDTO(reviews);
        CustomResponseWrapper<List<ReviewResponseDTO>> wrapper = CustomResponseWrapper.<List<ReviewResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("List of reviews for film with ID: %s", filmId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/{userId}/films")
    public ResponseEntity<CustomResponseWrapper<List<ReviewResponseDTO>>> getAllReviewsByUserId(@PathVariable("userId") Long userId) {
        List<Review> reviews = reviewService.getReviewsByUserId(userId);
        List<ReviewResponseDTO> response = reviewMapper.mapReviewToReviewResponseDTO(reviews);
        CustomResponseWrapper<List<ReviewResponseDTO>> wrapper = CustomResponseWrapper.<List<ReviewResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("List of reviews of user with ID: %s", userId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/reviews/films")
    public ResponseEntity<CustomResponseWrapper<List<ReviewResponseDTO>>> getReviewsByUserIdAndFilmId(@RequestParam("userId") Long userId,
                                                                                                      @RequestParam("filmId") Long filmId) {
        List<Review> reviews = reviewService.getReviewsByUserIdAndFilmId(userId, filmId);
        List<ReviewResponseDTO> response = reviewMapper.mapReviewToReviewResponseDTO(reviews);
        CustomResponseWrapper<List<ReviewResponseDTO>> wrapper = CustomResponseWrapper.<List<ReviewResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("List of reviews for film with ID: %s of user with ID: %s", filmId, userId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/{userId}/films/diary")
    public ResponseEntity<CustomResponseWrapper<List<ReviewResponseDTO>>> getDiary(@PathVariable("userId") Long userId) {
        List<Review> reviews = reviewService.getReviewsByUserIdAsDiary(userId);
        List<ReviewResponseDTO> response = reviewMapper.mapReviewToReviewResponseDTO(reviews);
        CustomResponseWrapper<List<ReviewResponseDTO>> wrapper = CustomResponseWrapper.<List<ReviewResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Diary films of user with ID: %s", userId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<CustomResponseWrapper<List<ReviewResponseDTO>>> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        List<ReviewResponseDTO> response = reviewMapper.mapReviewToReviewResponseDTO(reviews);
        CustomResponseWrapper<List<ReviewResponseDTO>> wrapper = CustomResponseWrapper.<List<ReviewResponseDTO>>builder()
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
}
