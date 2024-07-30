package com.seyan.activity.activity;

import com.seyan.activity.dto.ActivityAndReviewCreationDTO;
import com.seyan.activity.dto.ActivityOnFilmMapper;
import com.seyan.activity.exception.ActivityDeleteException;
import com.seyan.activity.exception.ActivityNotFoundException;

import com.seyan.activity.film.FilmClient;
import com.seyan.activity.responsewrapper.CustomResponseWrapper;
import com.seyan.activity.review.ReviewClient;
import com.seyan.activity.review.ReviewCreationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ActivityOnFilmService {
    private final ActivityOnFilmRepository activityRepository;
    private final ActivityOnFilmMapper activityMapper;
    private final ReviewClient reviewClient;
    private final FilmClient filmClient;

    public ActivityOnFilm getActivitiesForReviewLikes(List<Long> likedUsersIds) {
        //get user (username, followers, following)
        //get activity (rating, like)
        //get review (if present - link to page with particular user reviews for this film)
        return null;
    }

    //TODO get as likes
    public ActivityOnFilm getActivitiesForListLikes(List<Long> likedUsersIds) {
        return null;
    }

    //todo add external review entity
    @Transactional //todo interservice transaction (?)
    public ActivityOnFilm createOrUpdateActivity(ActivityAndReviewCreationDTO request) {
        ActivityOnFilm activity = activityMapper.mapActivityAndReviewCreationDTOToActivityOnFilm(request);

        if (request.reviewContent() != null || request.watchedOnDate() != null) {
            ReviewCreationDTO dto = activityMapper.mapActivityAndReviewCreationDTOToReviewCreationDTO(request);
            reviewClient.createReview(dto);

            activity.setIsInWatchlist(false);
            activity.setHasReview(true);
        }

        return activityRepository.save(activity);
    }

    //todo getWatchedListedLikedCount instead of related fields in Film entity
    // and get rid of FilmService calls from other methods

    public List<ActivityOnFilm> getWatchedFilmsActivities(Long userId) {
        return activityRepository.findWatchedFilmsActivities(userId);
    }

    public List<ActivityOnFilm> getWatchlist(Long userId) {
        List<ActivityOnFilm> watchlist = activityRepository.findWatchlistByUserId(userId);
        return watchlist.stream()
                .sorted(Comparator.comparing(ActivityOnFilm::getWatchlistAddDate).reversed())
                .toList();
    }

    public List<ActivityOnFilm> getLikedFilmsActivities(Long userId) {
        return activityRepository.findLikedFilmsActivities(userId);
    }

    //////////////////////////////////////////////// DEBUG METHODS
    public List<ActivityOnFilm> getAllActivities() {
        return activityRepository.findAll();
    }

    public List<ActivityOnFilm> getAllActivitiesByUserId(Long userId) {
        return activityRepository.findByUserId(userId);
    }

    public ActivityOnFilm getActivityById(ActivityOnFilmId id) {
        return activityRepository.findById(id).orElseThrow(() -> new ActivityNotFoundException(
                String.format("No film activity found with the provided ID: %s", id)
        ));
    }
    ////////////////////////////////////////////////

    //todo make default constructor (?)
    public ActivityOnFilm getOrCreateActivityById(ActivityOnFilmId id) {
        return activityRepository.findById(id).orElseGet(() -> ActivityOnFilm.builder()
                .id(id)
                .isWatched(false)
                .isLiked(false)
                .isInWatchlist(false)
                .rating(0.0)
                .hasReview(false)
                .build());
    }

    //todo kafka queue ?
    @Transactional
    public ActivityOnFilm updateIsLiked(Long userId, Long filmId) {
        ActivityOnFilm activity = getOrCreateActivityById(new ActivityOnFilmId(userId, filmId));
        if (activity.getIsLiked()) {
            activity.setIsLiked(false);
            filmClient.updateLikeCount(filmId, false);
            //filmService.addLikeCount(filmId, false);
            //userService.removeFilmFromLiked(userId, filmId);
        } else {
            activity.setIsLiked(true);
            filmClient.updateLikeCount(filmId, true);
            //filmService.addLikeCount(filmId, true);
            //userService.addFilmToLiked(userId, filmId);
        }
        return activityRepository.save(activity);
    }

    @Transactional
    public ActivityOnFilm updateRating(Long userId, Long filmId, Double rating) {
        ActivityOnFilm activity = getOrCreateActivityById(new ActivityOnFilmId(userId, filmId));
        activity.setRating(rating);
        activity.setIsWatched(true);
        activity.setIsInWatchlist(false);
        activityRepository.save(activity);
        Double avgRating = getFilmAvgRating(filmId);
        filmClient.updateAvgRating(filmId, rating);
        //filmService.updateAvgRating(filmId, avgRating);
        return activity;
    }

    @Transactional
    public ActivityOnFilm removeRating(Long userId, Long filmId) {
        ActivityOnFilm activity = getOrCreateActivityById(new ActivityOnFilmId(userId, filmId));
        activity.setRating(0.0);
        activityRepository.save(activity);
        Double avgRating = getFilmAvgRating(filmId);
        filmClient.updateAvgRating(filmId, avgRating);
        //filmService.updateAvgRating(filmId, avgRating);
        return activity;
    }

    private Double getFilmAvgRating(Long filmId) {
        return activityRepository.getFilmAvgRating(filmId).orElse(0.0);
    }

    @Transactional
    public ActivityOnFilm updateIsWatched(Long userId, Long filmId) {
        ActivityOnFilm activity = getOrCreateActivityById(new ActivityOnFilmId(userId, filmId));

        if (activity.getIsWatched()) {
            boolean isHasReviews = checkIfHasReviews(userId, filmId);
            if (activity.getRating() > 0.0 || isHasReviews) {
                throw new ActivityDeleteException(
                        String.format("Film with the provided ID has rating or reviews and cannot be removed from watched: %s", filmId)
                );
            }

            activity.setIsWatched(false);

            filmClient.updateWatchedCount(filmId, false);
            //filmService.addWatchedCount(filmId, false);

            //userService.removeFilmFromWatched(userId, filmId);
        } else {
            activity.setIsWatched(true);
            activity.setIsInWatchlist(false);
            filmClient.updateWatchedCount(filmId, true);
            //filmService.addWatchedCount(filmId, true);
            //userService.addFilmToWatched(userId, filmId);
        }

        return activityRepository.save(activity);
    }

    //todo return true if review service is not responding (on microservice layer)
    private boolean checkIfHasReviews(Long userId, Long filmId) {
        //int reviewCount = reviewService.countUserReviewsForFilm(userId, filmId);
        CustomResponseWrapper<Long> response = reviewClient.countUserReviewsForFilm(userId, filmId)
                .orElse(CustomResponseWrapper.<Long>builder().data(1L).build());
        Long reviewCount = response.getData();
        return reviewCount > 0;
    }

    public ActivityOnFilm updateIsInWatchlist(Long userId, Long filmId) {
        ActivityOnFilm activity = getOrCreateActivityById(new ActivityOnFilmId(userId, filmId));

        if (activity.getIsInWatchlist()) {
            activity.setIsInWatchlist(false);
        } else {
            activity.setIsInWatchlist(true);
            activity.setWatchlistAddDate(LocalDate.now());
        }

        return activityRepository.save(activity);
    }

    //todo deleteIfEmpty (?) probably i should never use this method
    private void deleteIfEmpty(ActivityOnFilm activity) {
        if (!activity.getIsWatched()
                & activity.getRating() == 0.0
                & !activity.getIsLiked()
                & !activity.getIsInWatchlist()
        ) {
            activityRepository.deleteById(activity.getId());
        }
    }

    public ActivityOnFilm updateHasReview(Long userId, Long filmId) {
        ActivityOnFilm activity = getOrCreateActivityById(new ActivityOnFilmId(userId, filmId));
        if (activity.getHasReview()) {
            activity.setHasReview(false);
        } else {
            activity.setHasReview(true);
        }
        return activityRepository.save(activity);
    }
}
