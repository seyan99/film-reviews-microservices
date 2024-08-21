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
import java.util.Optional;

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
    public ResponseEntity<CustomResponseWrapper<ReviewResponseDTO>> addReviewComment(
            @RequestParam("reviewId") Long reviewId, @RequestParam("commentId") Long commentId) {

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
    public ResponseEntity<CustomResponseWrapper<ReviewResponseDTO>> deleteReviewComment(
            @RequestParam("reviewId") Long reviewId, @RequestParam("commentId") Long commentId) {

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
    public ResponseEntity<CustomResponseWrapper<ReviewResponseDTO>> updateReviewLikes(
            @RequestParam("reviewId") Long reviewId, @RequestParam("userId") Long userId) {

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
    public ResponseEntity<CustomResponseWrapper<ReviewResponseDTO>> updateReview(
            @PathVariable("id") Long reviewId, @RequestBody @Valid ReviewUpdateDTO dto) {

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

    //todo rename method
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

    @GetMapping({"/film/{title}", "/film/{title}/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<PageableReviewResponseDTO>> getNewestReviews(
            @PathVariable String title, @PathVariable Optional<Integer> pageNo) {

        Page<Review> reviews;
        if (pageNo.isPresent()) {
            reviews = reviewService.getNewestReviews(title, pageNo.get());
        } else {
            reviews = reviewService.getNewestReviews(title, 1);
        }

        PageableReviewResponseDTO response = reviewMapper.mapReviewsPageToPageableReviewResponseDTO(reviews);
        CustomResponseWrapper<PageableReviewResponseDTO> wrapper = CustomResponseWrapper.<PageableReviewResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Newest reviews for film with title: %s", title))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping({"/film/{title}/by/added-earliest", "/film/{title}/by/added-earliest/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<PageableReviewResponseDTO>> getEarliestReviews(
            @PathVariable String title, @PathVariable Optional<Integer> pageNo) {

        Page<Review> reviews;
        if (pageNo.isPresent()) {
            reviews = reviewService.getEarliestReviews(title, pageNo.get());
        } else {
            reviews = reviewService.getEarliestReviews(title, 1);
        }

        PageableReviewResponseDTO response = reviewMapper.mapReviewsPageToPageableReviewResponseDTO(reviews);
        CustomResponseWrapper<PageableReviewResponseDTO> wrapper = CustomResponseWrapper.<PageableReviewResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Earliest reviews for film with title: %s", title))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping({"/film/{title}/by/popular", "/film/{title}/by/popular/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<PageableReviewResponseDTO>> getPopularReviews(
            @PathVariable String title, @PathVariable Optional<Integer> pageNo) {

        Page<Review> reviews;
        if (pageNo.isPresent()) {
            reviews = reviewService.getPopularReviews(title, pageNo.get());
        } else {
            reviews = reviewService.getPopularReviews(title, 1);
        }

        PageableReviewResponseDTO response = reviewMapper.mapReviewsPageToPageableReviewResponseDTO(reviews);
        CustomResponseWrapper<PageableReviewResponseDTO> wrapper = CustomResponseWrapper.<PageableReviewResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Popular reviews for film with title: %s", title))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping({"/film/{title}/by/entry-rating", "/film/{title}/by/entry-rating/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<PageableReviewResponseDTO>> getReviewsByHighestRating(
            @PathVariable String title, @PathVariable Optional<Integer> pageNo) {

        Page<Review> reviews;
        if (pageNo.isPresent()) {
            reviews = reviewService.getReviewsByHighestRating(title, pageNo.get());
        } else {
            reviews = reviewService.getReviewsByHighestRating(title, 1);
        }

        PageableReviewResponseDTO response = reviewMapper.mapReviewsPageToPageableReviewResponseDTO(reviews);
        CustomResponseWrapper<PageableReviewResponseDTO> wrapper = CustomResponseWrapper.<PageableReviewResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Reviews with highest rating for film with title: %s", title))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping({"/film/{title}/by/entry-rating-lowest", "/film/{title}/by/entry-rating-lowest/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<PageableReviewResponseDTO>> getReviewsByLowestRating(
            @PathVariable String title, @PathVariable Optional<Integer> pageNo) {

        Page<Review> reviews;
        if (pageNo.isPresent()) {
            reviews = reviewService.getReviewsByLowestRating(title, pageNo.get());
        } else {
            reviews = reviewService.getReviewsByLowestRating(title, 1);
        }

        PageableReviewResponseDTO response = reviewMapper.mapReviewsPageToPageableReviewResponseDTO(reviews);
        CustomResponseWrapper<PageableReviewResponseDTO> wrapper = CustomResponseWrapper.<PageableReviewResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Reviews with lowest rating for film with title: %s", title))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping({"/user/{username}", "/user/{username}/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<PageableReviewResponseDTO>> getAllReviewsByUsername(
            @PathVariable String username, @PathVariable Optional<Integer> pageNo) {

        Page<Review> reviews;
        if (pageNo.isPresent()) {
            reviews = reviewService.getReviewsByUsername(username, pageNo.get());
        } else {
            reviews = reviewService.getReviewsByUsername(username, 1);
        }

        PageableReviewResponseDTO response = reviewMapper.mapReviewsPageToPageableReviewResponseDTO(reviews);
        CustomResponseWrapper<PageableReviewResponseDTO> wrapper = CustomResponseWrapper.<PageableReviewResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Reviews of user: %s", username))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping({"/user/{username}/title/{title}", "/user/{username}/title/{title}/{reviewId}"})
    public ResponseEntity<CustomResponseWrapper<ReviewResponseDTO>> getReviewByUsernameAndTitle(
            @PathVariable String username,  @PathVariable String title, @PathVariable Optional<Long> reviewId) {

        Review review = reviewService.getReviewByUsernameAndTitle(username, title, reviewId);
        ReviewResponseDTO response = reviewMapper.mapReviewToReviewResponseDTO(review);
        CustomResponseWrapper<ReviewResponseDTO> wrapper = CustomResponseWrapper.<ReviewResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Review for film with title: %s of user: %s", title, username))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping({"/user/{username}/diary", "/user/{username}/diary/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<PageableReviewResponseDTO>> getDiary(
            @PathVariable String username, @PathVariable Optional<Integer> pageNo) {

        Page<Review> reviews;
        if (pageNo.isPresent()) {
            reviews = reviewService.getReviewsByUsernameAsDiary(username, pageNo.get());
        } else {
            reviews = reviewService.getReviewsByUsernameAsDiary(username, 1);
        }
        PageableReviewResponseDTO response = reviewMapper.mapReviewsPageToPageableReviewResponseDTO(reviews);
        CustomResponseWrapper<PageableReviewResponseDTO> wrapper = CustomResponseWrapper.<PageableReviewResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Diary films of user: %s", username))
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
