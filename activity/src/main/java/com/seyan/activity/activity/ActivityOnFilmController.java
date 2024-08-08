package com.seyan.activity.activity;

import com.seyan.activity.dto.ActivityAndReviewCreationDTO;
import com.seyan.activity.dto.ActivityOnFilmMapper;
import com.seyan.activity.dto.ActivityOnFilmResponseDTO;
import com.seyan.activity.handler.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/activities")
@RestController
public class ActivityOnFilmController {
    private final ActivityOnFilmService activityService;
    private final ActivityOnFilmMapper activityMapper;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllActivities() {
        List<ActivityOnFilm> activityList = activityService.getAllActivities();
        List<ActivityOnFilmResponseDTO> response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activityList);

        return ResponseHandler.generateResponse("All activities", HttpStatus.OK, response);
    }

    @GetMapping("/{userId}/activity")
    public ResponseEntity<Object> getAllActivitiesByUserId(@PathVariable("userId") Long userId) {
        List<ActivityOnFilm> activityList = activityService.getAllActivitiesByUserId(userId);
        List<ActivityOnFilmResponseDTO> response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activityList);

        return ResponseHandler.generateResponse(String.format("All activities of user with ID: %s", userId), HttpStatus.OK, response);
    }

    @GetMapping("/by-user-id-and-rating")
    public ResponseEntity<Object> getActivityByUserIdAndByRatingGreaterThan(
            @RequestParam("userId") Long userId, @RequestParam("rating") Double rating) {

        List<ActivityOnFilm> activityList = activityService.getActivityByUserIdAndByRatingGreaterThan(userId, rating);
        List<ActivityOnFilmResponseDTO> response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activityList);

        return ResponseHandler.generateResponse(String.format("All activities of user with ID: %s", userId), HttpStatus.OK, response);
    }

    @PostMapping("/create-update")
    public ResponseEntity<Object> createOrUpdateActivity(@RequestBody @Valid ActivityAndReviewCreationDTO request) {
        ActivityOnFilm activity = activityService.createOrUpdateActivity(request);
        ActivityOnFilmResponseDTO response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activity);

        return ResponseHandler.generateResponse("Film activity has been successfully created or updated", HttpStatus.OK, response);
    }

    @GetMapping("/{userId}/films")
    public ResponseEntity<Object> getWatchedFilmsActivities(@PathVariable("userId") Long userId) {
        List<ActivityOnFilm> activityList = activityService.getWatchedFilmsActivities(userId);
        List<ActivityOnFilmResponseDTO> response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activityList);

        return ResponseHandler.generateResponse(String.format("Watched films of user with ID: %s", userId), HttpStatus.OK, response);
    }

    @GetMapping("/{userId}/watchlist")
    public ResponseEntity<Object> getWatchlist(@PathVariable("userId") Long userId) {
        List<ActivityOnFilm> activityList = activityService.getWatchlist(userId);
        List<ActivityOnFilmResponseDTO> response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activityList);

        return ResponseHandler.generateResponse("Watchlist of user with ID: %s", HttpStatus.OK, response);
    }

    @GetMapping("/{userId}/likes/films")
    public ResponseEntity<Object> getLikedFilmsActivities(@PathVariable("userId") Long userId) {
        List<ActivityOnFilm> activityList = activityService.getLikedFilmsActivities(userId);
        List<ActivityOnFilmResponseDTO> response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activityList);

        return ResponseHandler.generateResponse("Liked films of user with ID: %s", HttpStatus.OK, response);
    }

    @GetMapping("/on-film")
    public ResponseEntity<Object> getFilmActivity(@RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId) {
        ActivityOnFilm activity = activityService.getOrCreateActivityById(new ActivityOnFilmId(userId, filmId));
        ActivityOnFilmResponseDTO response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activity);

        return ResponseHandler.generateResponse("User with ID %s activity for film with ID: %s", HttpStatus.OK, response);
    }

    @PatchMapping("/update-has-review")
    public ResponseEntity<Object> updateHasReview(@RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId) {
        ActivityOnFilm activity = activityService.updateHasReview(userId, filmId);
        ActivityOnFilmResponseDTO response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activity);

        return ResponseHandler.generateResponse("Has review status has been updated", HttpStatus.OK, response);
    }

    @PatchMapping("/update-is-liked")
    public ResponseEntity<Object> updateIsLiked(
            @RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId) {

        ActivityOnFilm activity = activityService.updateIsLiked(userId, filmId);
        ActivityOnFilmResponseDTO response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activity);

        return ResponseHandler.generateResponse("Like status has been updated", HttpStatus.OK, response);
    }

    @PatchMapping("/update-rating")
    public ResponseEntity<Object> updateRating(
            @RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId, @RequestBody Double rating) {

        ActivityOnFilm activity = activityService.updateRating(userId, filmId, rating);
        ActivityOnFilmResponseDTO response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activity);

        return ResponseHandler.generateResponse("Rating has been updated", HttpStatus.OK, response);
    }

    @PatchMapping("/remove-rating")
    public ResponseEntity<Object> removeRating(
            @RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId) {

        ActivityOnFilm activity = activityService.removeRating(userId, filmId);
        ActivityOnFilmResponseDTO response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activity);

        return ResponseHandler.generateResponse("Rating has been removed", HttpStatus.OK, response);
    }

    @PatchMapping("/update-is-watched")
    public ResponseEntity<Object> updateIsWatched(
            @RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId) {

        ActivityOnFilm activity = activityService.updateIsWatched(userId, filmId);
        ActivityOnFilmResponseDTO response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activity);

        return ResponseHandler.generateResponse("Is watched status has been updated", HttpStatus.OK, response);
    }

    @PatchMapping("/update-is-in-watchlist")
    public ResponseEntity<Object> updateIsInWatchlist(
            @RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId) {

        ActivityOnFilm activity = activityService.updateIsInWatchlist(userId, filmId);
        ActivityOnFilmResponseDTO response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activity);

        return ResponseHandler.generateResponse("Is in watchlist status has been updated", HttpStatus.OK, response);
    }
}
