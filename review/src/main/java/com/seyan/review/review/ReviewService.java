package com.seyan.review.review;

import com.seyan.review.external.activity.ActivityClient;
import com.seyan.review.dto.ReviewCreationDTO;
import com.seyan.review.dto.ReviewMapper;
import com.seyan.review.dto.ReviewUpdateDTO;
import com.seyan.review.exception.ReviewNotFoundException;
import com.seyan.review.external.activity.FilmClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ActivityClient activityClient;
    private final FilmClient filmClient;

    public Review addReviewComment(Long reviewId, Long commentId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException(
                String.format("No review found with the provided ID: %s", reviewId)
        ));

        review.getCommentIds().add(commentId);
        return reviewRepository.save(review);
    }

    public Review deleteReviewComment(Long reviewId, Long commentId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException(
                String.format("No review found with the provided ID: %s", reviewId)
        ));

        review.getCommentIds().remove(commentId);
        return reviewRepository.save(review);
    }

    public Review updateReviewLikes(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException(
                String.format("No review found with the provided ID: %s", reviewId)
        ));

        if (review.getLikedUsersIds().contains(userId)) {
            review.getLikedUsersIds().remove(userId);
            review.setLikedCount(review.getLikedCount() - 1);
        } else {
            review.getLikedUsersIds().add(userId);
            review.setLikedCount(review.getLikedCount() + 1);
        }
        return reviewRepository.save(review);
    }

    @Transactional
    public Review createReview(ReviewCreationDTO dto) {
        int reviewsCount = reviewRepository.countByUserIdAndFilmIdAndContentNotNull(dto.userId(), dto.filmId());

        Review review = reviewMapper.mapReviewCreationDTOToReview(dto);
        Review saved = reviewRepository.save(review);

        if (reviewsCount < 1 && dto.content() != null) {
            activityClient.updateHasReview(dto.userId(), dto.filmId());
        }
        filmClient.updateReviewCount(dto.filmId(), true);

        return saved;
    }

    //todo has your activity also if diary not yours
    public Page<Review> getReviewsByUsernameAsDiary(String username, Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "creationDate");
        Pageable pageable = PageRequest.of(pageNo - 1, 25, sort);
        return reviewRepository.findByUsernameAndWatchedOnDateNotNull(username, pageable);
    }

    public Review updateReview(Long reviewId, ReviewUpdateDTO dto) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewNotFoundException(
                String.format("Cannot update review:: No review found with the provided ID: %s", reviewId)
        ));

        Review mapped = reviewMapper.mapReviewUpdateDTOToReview(dto, review);
        return reviewRepository.save(mapped);
    }

    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new ReviewNotFoundException(
                String.format("Cannot delete review:: No review found with the provided ID: %s", id)));

        reviewRepository.deleteById(id);

        int reviewsCount = reviewRepository.countByUserIdAndFilmIdAndContentNotNull(review.getUserId(), review.getFilmId());

        if (reviewsCount == 0) {
            activityClient.updateHasReview(review.getUserId(), review.getFilmId());
        }
        filmClient.updateReviewCount(review.getFilmId(), false);
    }

    public int countUserReviewsForFilm(Long userId, Long filmId) {
        return reviewRepository.countByUserIdAndFilmId(userId, filmId);
    }

    public List<Long> getFilmIdsBasedOnReviewDateAfter(LocalDate date) {
        List<Long> idList = reviewRepository.findFilmIdBasedOnReviewCreationDateAfter(date);
        List<Long> idListSorted = idList.stream().sorted(Comparator.comparing(it -> Collections.frequency(idList, it)).reversed()).distinct().toList();
        return idListSorted;
    }

    /*public List<Long> getFilmIdsBasedOnReviewDateAfter(LocalDate date) {
        return reviewRepository.findFilmIdBasedOnReviewCreationDateAfter(date);
    }*/

    public List<Long> getAllFilmIds() {
        return reviewRepository.findAllFilmIds();
    }

    public Map<String, List<Review>> getLatestAndPopularReviewsForFilm(Long filmId) {
        //Sort sort = Sort.by("likedUsersIds").descending();
        List<Review> latest = reviewRepository.findByFilmIdTop3ByOrderByCreationDateDesc(filmId);
        List<Review> popular = reviewRepository.findByFilmIdAndTop3ByLikedUsersIdsDesc(filmId);
        HashMap<String, List<Review>> reviews = new HashMap<>();
        reviews.put("latest", latest);
        reviews.put("popular", popular);
        return reviews;
    }

    public Page<Review> getNewestReviews(String title, Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "creationDate");
        Pageable pageable = PageRequest.of(pageNo - 1, 12, sort);
        return reviewRepository.findByTitleAndContentNotNull(title, pageable);
    }

    public Page<Review> getEarliestReviews(String title, Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.ASC, "creationDate");
        Pageable pageable = PageRequest.of(pageNo - 1, 12, sort);
        return reviewRepository.findByTitleAndContentNotNull(title, pageable);
    }

    public Page<Review> getPopularReviews(String title, Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "likedCount");
        Pageable pageable = PageRequest.of(pageNo - 1, 12, sort);
        return reviewRepository.findByTitleAndContentNotNull(title, pageable);
    }

    public Page<Review> getReviewsByHighestRating(String title, Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "rating");
        Pageable pageable = PageRequest.of(pageNo - 1, 12, sort);
        return reviewRepository.findByTitleAndContentNotNull(title, pageable);
    }

    public Page<Review> getReviewsByLowestRating(String title, Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.ASC, "rating");
        Pageable pageable = PageRequest.of(pageNo - 1, 12, sort);
        return reviewRepository.findByTitleAndContentNotNull(title, pageable);
    }

    public Page<Review> getReviewsByUsername(String username, Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.ASC, "creationDate");
        Pageable pageable = PageRequest.of(pageNo - 1, 12, sort);
        return reviewRepository.findByUsernameAndContentNotNull(username, pageable);
    }

    //returns by id or earliest available
    public Review getReviewByUsernameAndTitle(String username, String title, Optional<Long> reviewId) {
        if (reviewId.isPresent()) {
            return reviewRepository.findById(reviewId.get()).orElseThrow(() -> new ReviewNotFoundException(
                    String.format("No review found with the provided ID: %s", reviewId)
            ));
        } else {
            Sort sort = Sort.by(Sort.Direction.ASC, "id");
            List<Review> reviews = reviewRepository.findByUsernameAndTitleAndContentNotNull(username, title, sort);
            return reviews.get(0);
        }
    }
}
