package com.seyan.activity.activity;

import com.seyan.activity.dto.ActivityAndReviewCreationDTO;
import com.seyan.activity.dto.ActivityMapper;
import com.seyan.activity.dto.ActivityResponseDTO;
import com.seyan.activity.external.review.ReviewCreationDTO;
import com.seyan.activity.messaging.FilmMessageProducer;
import com.seyan.activity.messaging.ReviewMessageProducer;
import com.seyan.activity.responsewrapper.CustomResponseWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/v1/activities")
@RestController
public class ActivityController {
    private final ActivityService activityService;
    private final ActivityMapper activityMapper;
    private final ReviewMessageProducer reviewMessageProducer;
    private final FilmMessageProducer filmMessageProducer;

    @GetMapping("/all")
    public ResponseEntity<CustomResponseWrapper<List<ActivityResponseDTO>>> getAllActivities() {
        List<Activity> activityList = activityService.getAllActivities();
        List<ActivityResponseDTO> response = activityMapper.mapActivityToActivityResponseDTO(activityList);
        CustomResponseWrapper<List<ActivityResponseDTO>> wrapper = CustomResponseWrapper.<List<ActivityResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("All activities")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/{userId}/activity")
    public ResponseEntity<CustomResponseWrapper<List<ActivityResponseDTO>>> getAllActivitiesByUserId(@PathVariable("userId") Long userId) {
        List<Activity> activityList = activityService.getAllActivitiesByUserId(userId);
        List<ActivityResponseDTO> response = activityMapper.mapActivityToActivityResponseDTO(activityList);
        CustomResponseWrapper<List<ActivityResponseDTO>> wrapper = CustomResponseWrapper.<List<ActivityResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("All activities of user with ID: %s", userId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/by-user-id-and-rating")
    public ResponseEntity<CustomResponseWrapper<List<ActivityResponseDTO>>> getActivityByUserIdAndByRatingGreaterThan(@RequestParam("userId") Long userId,
                                                                                                                      @RequestParam("rating") Double rating) {
        List<Activity> activityList = activityService.getActivityByUserIdAndByRatingGreaterThan(userId, rating);
        List<ActivityResponseDTO> response = activityMapper.mapActivityToActivityResponseDTO(activityList);
        CustomResponseWrapper<List<ActivityResponseDTO>> wrapper = CustomResponseWrapper.<List<ActivityResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("All activities of user with ID: %s", userId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PostMapping("/create-update")
    public ResponseEntity<CustomResponseWrapper<ActivityResponseDTO>> createOrUpdateActivity(@RequestBody @Valid ActivityAndReviewCreationDTO request) {
        Activity activity = activityService.createOrUpdateActivity(request);
        ActivityResponseDTO response = activityMapper.mapActivityToActivityResponseDTO(activity);

        if (request.reviewContent() != null || request.watchedOnDate() != null) {
            ReviewCreationDTO dto = activityMapper.mapActivityAndReviewCreationDTOToReviewCreationDTO(request);
            reviewMessageProducer.createReview(dto);
            filmMessageProducer.updateWatchedCount(request.filmId(), true);
        }

        CustomResponseWrapper<ActivityResponseDTO> wrapper = CustomResponseWrapper.<ActivityResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Film activity has been successfully created or updated")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/{userId}/films")
    public ResponseEntity<CustomResponseWrapper<List<ActivityResponseDTO>>> getWatchedFilmsActivities(@PathVariable("userId") Long userId) {
        List<Activity> activityList = activityService.getWatchedFilmsActivities(userId);
        List<ActivityResponseDTO> response = activityMapper.mapActivityToActivityResponseDTO(activityList);
        CustomResponseWrapper<List<ActivityResponseDTO>> wrapper = CustomResponseWrapper.<List<ActivityResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Watched films of user with ID: %s", userId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/{userId}/watchlist")
    public ResponseEntity<CustomResponseWrapper<List<ActivityResponseDTO>>> getWatchlist(@PathVariable("userId") Long userId) {
        List<Activity> activityList = activityService.getWatchlist(userId);
        List<ActivityResponseDTO> response = activityMapper.mapActivityToActivityResponseDTO(activityList);
        CustomResponseWrapper<List<ActivityResponseDTO>> wrapper = CustomResponseWrapper.<List<ActivityResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Watchlist of user with ID: %s", userId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/{userId}/likes/films")
    public ResponseEntity<CustomResponseWrapper<List<ActivityResponseDTO>>> getLikedFilmsActivities(@PathVariable("userId") Long userId) {
        List<Activity> activityList = activityService.getLikedFilmsActivities(userId);
        List<ActivityResponseDTO> response = activityMapper.mapActivityToActivityResponseDTO(activityList);
        CustomResponseWrapper<List<ActivityResponseDTO>> wrapper = CustomResponseWrapper.<List<ActivityResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Liked films of user with ID: %s", userId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/on-film")
    public ResponseEntity<CustomResponseWrapper<ActivityResponseDTO>> getFilmActivity(@RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId) {
        Activity activity = activityService.getOrCreateActivityById(new ActivityId(userId, filmId));
        ActivityResponseDTO response = activityMapper.mapActivityToActivityResponseDTO(activity);
        CustomResponseWrapper<ActivityResponseDTO> wrapper = CustomResponseWrapper.<ActivityResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("User with ID %s activity for film with ID: %s", userId, filmId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PutMapping("/update-has-review")
    public ResponseEntity<CustomResponseWrapper<ActivityResponseDTO>> updateHasReview(
            @RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId) {
        Activity activity = activityService.updateHasReview(userId, filmId);
        ActivityResponseDTO response = activityMapper.mapActivityToActivityResponseDTO(activity);
        CustomResponseWrapper<ActivityResponseDTO> wrapper = CustomResponseWrapper.<ActivityResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Has review status has been updated")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PatchMapping("/update-is-liked")
    public ResponseEntity<CustomResponseWrapper<ActivityResponseDTO>> updateIsLiked(
            @RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId) {
        Activity activity = activityService.updateIsLiked(userId, filmId);
        ActivityResponseDTO response = activityMapper.mapActivityToActivityResponseDTO(activity);

        if (activity.getIsLiked()) {
            filmMessageProducer.updateLikeCount(filmId, true);
        } else {
            filmMessageProducer.updateLikeCount(filmId, false);
        }

        CustomResponseWrapper<ActivityResponseDTO> wrapper = CustomResponseWrapper.<ActivityResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Like status has been updated")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PatchMapping("/update-rating")
    public ResponseEntity<CustomResponseWrapper<ActivityResponseDTO>> updateRating(
            @RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId, @RequestBody Double rating) {
        Activity activity = activityService.updateRating(userId, filmId, rating);
        ActivityResponseDTO response = activityMapper.mapActivityToActivityResponseDTO(activity);

        filmMessageProducer.updateAvgRating(filmId, true);

        CustomResponseWrapper<ActivityResponseDTO> wrapper = CustomResponseWrapper.<ActivityResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Rating has been updated")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PatchMapping("/remove-rating")
    public ResponseEntity<CustomResponseWrapper<ActivityResponseDTO>> removeRating(
            @RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId) {

        Activity activity = activityService.removeRating(userId, filmId);
        ActivityResponseDTO response = activityMapper.mapActivityToActivityResponseDTO(activity);

        filmMessageProducer.updateAvgRating(filmId, true);

        CustomResponseWrapper<ActivityResponseDTO> wrapper = CustomResponseWrapper.<ActivityResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Rating has been removed")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/avg-rating")
    public ResponseEntity<CustomResponseWrapper<Double>> getAvgRating(@RequestParam("filmId") Long filmId) {
        Double response = activityService.getFilmAvgRating(filmId);
        CustomResponseWrapper<Double> wrapper = CustomResponseWrapper.<Double>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Avg rating for film with ID: %s", filmId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PatchMapping("/update-is-watched")
    public ResponseEntity<CustomResponseWrapper<ActivityResponseDTO>> updateIsWatched(
            @RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId) {

        Map<Boolean, Activity> activity = activityService.updateIsWatched(userId, filmId);
        ActivityResponseDTO response = activityMapper.mapActivityToActivityResponseDTO(activity.values().stream().findFirst().get());

        filmMessageProducer.updateWatchedCount(filmId, activity.keySet().stream().findFirst().get());

        CustomResponseWrapper<ActivityResponseDTO> wrapper = CustomResponseWrapper.<ActivityResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Is watched status has been updated")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PatchMapping("/update-is-in-watchlist")
    public ResponseEntity<CustomResponseWrapper<ActivityResponseDTO>> updateIsInWatchlist(
            @RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId) {

        Activity activity = activityService.updateIsInWatchlist(userId, filmId);
        ActivityResponseDTO response = activityMapper.mapActivityToActivityResponseDTO(activity);
        CustomResponseWrapper<ActivityResponseDTO> wrapper = CustomResponseWrapper.<ActivityResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Is in watchlist status has been updated")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }
}
