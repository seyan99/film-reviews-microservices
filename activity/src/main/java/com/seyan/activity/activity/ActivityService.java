package com.seyan.activity.activity;

import com.seyan.activity.dto.ActivityAndReviewCreationDTO;
import com.seyan.activity.dto.ActivityMapper;
import com.seyan.activity.exception.ActivityDeleteException;
import com.seyan.activity.exception.ActivityNotFoundException;
import com.seyan.activity.external.film.FilmClient;
import com.seyan.activity.responsewrapper.CustomResponseWrapper;
import com.seyan.activity.external.review.ReviewClient;
import com.seyan.activity.external.review.ReviewCreationDTO;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;
    private final ReviewClient reviewClient;
    private final FilmClient filmClient;

    public List<Activity> getActivityByUserIdAndByRatingGreaterThan(Long userId, Double rating) {
        return activityRepository.findActivityByUserIdAndByRatingGreaterThan(userId, rating);
    }

    /*@Transactional
    public Activity createOrUpdateActivity(ActivityAndReviewCreationDTO request) {
        Activity activity = activityMapper.mapActivityAndReviewCreationDTOToActivity(request);

        if (request.reviewContent() != null || request.watchedOnDate() != null) {
            ReviewCreationDTO dto = activityMapper.mapActivityAndReviewCreationDTOToReviewCreationDTO(request);
            reviewClient.createReview(dto);

            activity.setIsInWatchlist(false);
            activity.setHasReview(true);
        }

        return activityRepository.save(activity);
    }*/

    public Activity createOrUpdateActivity(ActivityAndReviewCreationDTO request) {
        Activity activity = activityMapper.mapActivityAndReviewCreationDTOToActivity(request);

        if (request.reviewContent() != null || request.watchedOnDate() != null) {
            activity.setIsInWatchlist(false);
            activity.setHasReview(true);
        }

        return activityRepository.save(activity);
    }

    public List<Activity> getWatchedFilmsActivities(Long userId) {
        return activityRepository.findWatchedFilmsActivities(userId);
    }

    public List<Activity> getWatchlist(Long userId) {
        List<Activity> watchlist = activityRepository.findWatchlistByUserId(userId);
        return watchlist.stream()
                .sorted(Comparator.comparing(Activity::getWatchlistAddDate).reversed())
                .toList();
    }

    public List<Activity> getLikedFilmsActivities(Long userId) {
        return activityRepository.findLikedFilmsActivities(userId);
    }

    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    public List<Activity> getAllActivitiesByUserId(Long userId) {
        return activityRepository.findByUserId(userId);
    }

    public Activity getActivityById(ActivityId id) {
        return activityRepository.findById(id).orElseThrow(() -> new ActivityNotFoundException(
                String.format("No film activity found with the provided ID: %s", id)
        ));
    }

    public Activity getOrCreateActivityById(ActivityId id) {
        return activityRepository.findById(id).orElseGet(() -> Activity.builder()
                .id(id)
                .isWatched(false)
                .isLiked(false)
                .isInWatchlist(false)
                .rating(0.0)
                .hasReview(false)
                .build());
    }

    /*@Transactional
    public Activity updateIsLiked(Long userId, Long filmId) {
        Activity activity = getOrCreateActivityById(new ActivityId(userId, filmId));
        if (activity.getIsLiked()) {
            activity.setIsLiked(false);
            filmClient.updateLikeCount(filmId, false);
        } else {
            activity.setIsLiked(true);
            filmClient.updateLikeCount(filmId, true);
        }
        return activityRepository.save(activity);
    }*/

    public Activity updateIsLiked(Long userId, Long filmId) {
        Activity activity = getOrCreateActivityById(new ActivityId(userId, filmId));
        if (activity.getIsLiked()) {
            activity.setIsLiked(false);
        } else {
            activity.setIsLiked(true);
        }
        return activityRepository.save(activity);
    }

    /*@Transactional
    public Activity updateRating(Long userId, Long filmId, Double rating) {
        Activity activity = getOrCreateActivityById(new ActivityId(userId, filmId));
        activity.setRating(rating);
        activity.setIsWatched(true);
        activity.setIsInWatchlist(false);
        activityRepository.save(activity);
        Double avgRating = getFilmAvgRating(filmId);
        filmClient.updateAvgRating(filmId, avgRating);
        return activity;
    }*/

    public Activity updateRating(Long userId, Long filmId, Double rating) {
        Activity activity = getOrCreateActivityById(new ActivityId(userId, filmId));
        activity.setRating(rating);
        activity.setIsWatched(true);
        activity.setIsInWatchlist(false);
        activityRepository.save(activity);
        return activity;
    }

    /*@Transactional
    public Activity removeRating(Long userId, Long filmId) {
        Activity activity = getOrCreateActivityById(new ActivityId(userId, filmId));
        activity.setRating(0.0);
        activityRepository.save(activity);
        Double avgRating = getFilmAvgRating(filmId);
        filmClient.updateAvgRating(filmId, avgRating);
        return activity;
    }*/

    public Activity removeRating(Long userId, Long filmId) {
        Activity activity = getOrCreateActivityById(new ActivityId(userId, filmId));
        activity.setRating(0.0);
        activityRepository.save(activity);
        return activity;
    }

    public Double getFilmAvgRating(Long filmId) {
        return activityRepository.getFilmAvgRating(filmId).orElse(0.0);
    }

    /*@Transactional
    public Activity updateIsWatched(Long userId, Long filmId) {
        Activity activity = getOrCreateActivityById(new ActivityId(userId, filmId));

        if (activity.getIsWatched()) {
            boolean isHasReviews = checkIfHasReviews(userId, filmId);
            if (activity.getRating() > 0.0 || isHasReviews) {
                throw new ActivityDeleteException(
                        String.format("Film with the provided ID has rating or reviews and cannot be removed from watched: %s", filmId)
                );
            }
            activity.setIsWatched(false);
            filmClient.updateWatchedCount(filmId, false);
        } else {
            activity.setIsWatched(true);
            activity.setIsInWatchlist(false);
            filmClient.updateWatchedCount(filmId, true);
        }
        return activityRepository.save(activity);
    }*/

    public Map<Boolean, Activity> updateIsWatched(Long userId, Long filmId) {
        Activity activity = getOrCreateActivityById(new ActivityId(userId, filmId));

        if (activity.getIsWatched()) {
            boolean isHasReviews = checkIfHasReviews(userId, filmId);
            if (activity.getRating() > 0.0 || isHasReviews) {
                throw new ActivityDeleteException(
                        String.format("Film with the provided ID has rating or reviews and cannot be removed from watched: %s", filmId)
                );
            }
            activity.setIsWatched(false);
            activityRepository.save(activity);
            //return false;
            return new HashMap<>(){{put(false, activity);}};
        } else {
            activity.setIsWatched(true);
            activity.setIsInWatchlist(false);
            activityRepository.save(activity);
            //return true;
            return new HashMap<>(){{put(true, activity);}};
        }
    }

    @RateLimiter(name = "responseBreaker", fallbackMethod = "checkIfHasReviewsFallback")
    private boolean checkIfHasReviews(Long userId, Long filmId) {
        CustomResponseWrapper<Long> response = reviewClient.countUserReviewsForFilm(userId, filmId)
                .orElse(CustomResponseWrapper.<Long>builder().data(1L).build());
        Long reviewCount = response.getData();
        return reviewCount > 0;
    }

    private boolean checkIfHasReviewsFallback(Exception e) {
        return true;
    }

    public Activity updateIsInWatchlist(Long userId, Long filmId) {
        Activity activity = getOrCreateActivityById(new ActivityId(userId, filmId));

        if (activity.getIsInWatchlist()) {
            activity.setIsInWatchlist(false);
        } else {
            activity.setIsInWatchlist(true);
            activity.setWatchlistAddDate(LocalDate.now());
        }

        return activityRepository.save(activity);
    }

    private void deleteIfEmpty(Activity activity) {
        if (!activity.getIsWatched()
                & activity.getRating() == 0.0
                & !activity.getIsLiked()
                & !activity.getIsInWatchlist()
        ) {
            activityRepository.deleteById(activity.getId());
        }
    }

    public Activity updateHasReview(Long userId, Long filmId) {
        Activity activity = getOrCreateActivityById(new ActivityId(userId, filmId));
        if (activity.getHasReview()) {
            activity.setHasReview(false);
        } else {
            activity.setHasReview(true);
        }
        return activityRepository.save(activity);
    }
}
