package com.seyan.review.review;

import com.seyan.reviewmonolith.activity.ActivityOnFilmService;
import com.seyan.reviewmonolith.activity.dto.ActivityAndReviewCreationDTO;
import com.seyan.reviewmonolith.comment.CommentService;
import com.seyan.reviewmonolith.exception.review.ReviewNotFoundException;
import com.seyan.reviewmonolith.review.dto.ReviewCreationDTO;
import com.seyan.reviewmonolith.review.dto.ReviewMapper;
import com.seyan.reviewmonolith.review.dto.ReviewUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    private final CommentService commentService;

    private final ActivityOnFilmService activityOnFilmService;

    public void addReviewComment(Long reviewId, Long commentId) {
        //todo add review comment
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException(
                String.format("No review found with the provided ID: %s", reviewId)
        ));

        review.getCommentIds().add(commentId);
        reviewRepository.save(review);
    }

    public void deleteReviewComment(Long reviewId, Long commentId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException(
                String.format("No review found with the provided ID: %s", reviewId)
        ));

        review.getCommentIds().remove(commentId);
        reviewRepository.save(review);
    }

    public Review updateReviewLikes(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException(
                String.format("No review found with the provided ID: %s", reviewId)
        ));

        if (review.getLikedUsersIds().contains(userId)) {
            review.getLikedUsersIds().remove(userId);
            //review.setLikeCount(review.getLikeCount() + 1);
        } else {
            review.getLikedUsersIds().add(userId);
            //review.setLikeCount(review.getLikeCount() - 1);
        }
        return reviewRepository.save(review);
    }

    @Transactional //todo interservice transaction
    public Review createReview(ReviewCreationDTO dto) {
        int reviewsCount = reviewRepository.countByUserIdAndFilmIdAndContentNotNull(dto.userId(), dto.filmId());

        Review review = reviewMapper.mapReviewCreationDTOToReview(dto);
        Review saved = reviewRepository.save(review);

        if (reviewsCount < 1 && dto.content() != null) {
            activityOnFilmService.updateHasReview(dto.userId(), dto.filmId());
        }

        return saved;
    }

    public Review createReview(ActivityAndReviewCreationDTO request) {
        Review review = reviewMapper.mapActivityReviewDiaryRequestToReview(request);
        return reviewRepository.save(review);
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new ReviewNotFoundException(
                String.format("No review found with the provided ID: %s", id)
        ));
    }

    //todo add sorting by your reviews and your network reviews
    public List<Review> getReviewsByFilmId(Long filmId) {
        return reviewRepository.findByFilmIdAndContentNotNull(filmId).stream()
                .sorted(Comparator.comparing(Review::getCreationDate).reversed())
                .toList();
    }

    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserIdAndContentNotNull(userId).stream()
                .sorted(Comparator.comparing(Review::getCreationDate).reversed())
                .toList();
    }

    public List<Review> getReviewsByUserIdAndFilmId(Long userId, Long filmId) {
        return reviewRepository.findByUserIdAndFilmIdAndContentNotNull(userId, filmId).stream()
                .sorted(Comparator.comparing(Review::getCreationDate).reversed())
                .toList();
    }

    public List<Review> getReviewsByUserIdAsDiary(Long userId) {
        return reviewRepository.findByUserIdAndWatchedOnDateNotNull(userId).stream()
                .sorted(Comparator.comparing(Review::getCreationDate).reversed())
                .toList();
    }

    //todo get by /username/film/film-title

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review updateReview(Long reviewId, ReviewUpdateDTO dto) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException(
                String.format("Cannot update review:: No review found with the provided ID: %s", reviewId)
        ));

        Review mapped = reviewMapper.mapReviewUpdateDTOToReview(dto, review);
        return reviewRepository.save(mapped);
    }


    @Transactional //todo interservice transaction
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new ReviewNotFoundException(
                String.format("Cannot delete review:: No review found with the provided ID: %s", id)));

        reviewRepository.deleteById(id);

        int reviewsCount = reviewRepository.countByUserIdAndFilmIdAndContentNotNull(review.getUserId(), review.getFilmId());

        if (reviewsCount == 0) {
            activityOnFilmService.updateHasReview(review.getUserId(), review.getFilmId());
        }
    }

    public int countUserReviewsForFilm(Long userId, Long filmId) {
        return reviewRepository.countByUserIdAndFilmId(userId, filmId);
    }

    public List<Long> getFilmIdBasedOnReviewDateAfter(LocalDate date) {
        return reviewRepository.findFilmIdBasedOnReviewCreationDateAfter(date);
    }

    public List<Long> getFilmIdBasedOnReviewDateBefore(LocalDate date) {
        return reviewRepository.findFilmIdBasedOnReviewCreationDateBefore(date);
    }

    public List<Long> getAllFilmIds() {
        return reviewRepository.findAllFilmIds();
    }
}
