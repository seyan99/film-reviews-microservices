package com.seyan.activity.activity;

import com.seyan.activity.dto.ActivityAndReviewCreationDTO;
import com.seyan.activity.dto.ActivityOnFilmMapper;
import com.seyan.activity.dto.ActivityOnFilmResponseDTO;
import com.seyan.activity.responsewrapper.CustomResponseWrapper;
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
    public ResponseEntity<CustomResponseWrapper<List<ActivityOnFilmResponseDTO>>> getAllActivities() {
        List<ActivityOnFilm> activityList = activityService.getAllActivities();
        List<ActivityOnFilmResponseDTO> response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activityList);
        CustomResponseWrapper<List<ActivityOnFilmResponseDTO>> wrapper = CustomResponseWrapper.<List<ActivityOnFilmResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("All activities")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/{userId}/activity")
    public ResponseEntity<CustomResponseWrapper<List<ActivityOnFilmResponseDTO>>> getAllActivitiesByUserId(@PathVariable("userId") Long userId) {
        List<ActivityOnFilm> activityList = activityService.getAllActivitiesByUserId(userId);
        List<ActivityOnFilmResponseDTO> response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activityList);
        CustomResponseWrapper<List<ActivityOnFilmResponseDTO>> wrapper = CustomResponseWrapper.<List<ActivityOnFilmResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("All activities of user with ID: %s", userId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/by-user-id-and-rating")
    public ResponseEntity<CustomResponseWrapper<List<ActivityOnFilmResponseDTO>>> getActivityByUserIdAndByRatingGreaterThan(@RequestParam("userId") Long userId,
                                                                                                                            @RequestParam("rating") Double rating) {
        List<ActivityOnFilm> activityList = activityService.getActivityByUserIdAndByRatingGreaterThan(userId, rating);
        List<ActivityOnFilmResponseDTO> response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activityList);
        CustomResponseWrapper<List<ActivityOnFilmResponseDTO>> wrapper = CustomResponseWrapper.<List<ActivityOnFilmResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("All activities of user with ID: %s", userId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PostMapping("/create-update")
    public ResponseEntity<CustomResponseWrapper<ActivityOnFilmResponseDTO>> createOrUpdateActivity(@RequestBody @Valid ActivityAndReviewCreationDTO request) {
        ActivityOnFilm activity = activityService.createOrUpdateActivity(request);
        ActivityOnFilmResponseDTO response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activity);
        CustomResponseWrapper<ActivityOnFilmResponseDTO> wrapper = CustomResponseWrapper.<ActivityOnFilmResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Film activity has been successfully created or updated")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/{userId}/films")
    public ResponseEntity<CustomResponseWrapper<List<ActivityOnFilmResponseDTO>>> getWatchedFilmsActivities(@PathVariable("userId") Long userId) {
        List<ActivityOnFilm> activityList = activityService.getWatchedFilmsActivities(userId);
        List<ActivityOnFilmResponseDTO> response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activityList);
        CustomResponseWrapper<List<ActivityOnFilmResponseDTO>> wrapper = CustomResponseWrapper.<List<ActivityOnFilmResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Watched films of user with ID: %s", userId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/{userId}/watchlist")
    public ResponseEntity<CustomResponseWrapper<List<ActivityOnFilmResponseDTO>>> getWatchlist(@PathVariable("userId") Long userId) {
        List<ActivityOnFilm> activityList = activityService.getWatchlist(userId);
        List<ActivityOnFilmResponseDTO> response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activityList);
        CustomResponseWrapper<List<ActivityOnFilmResponseDTO>> wrapper = CustomResponseWrapper.<List<ActivityOnFilmResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Watchlist of user with ID: %s", userId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/{userId}/likes/films")
    public ResponseEntity<CustomResponseWrapper<List<ActivityOnFilmResponseDTO>>> getLikedFilmsActivities(@PathVariable("userId") Long userId) {
        List<ActivityOnFilm> activityList = activityService.getLikedFilmsActivities(userId);
        List<ActivityOnFilmResponseDTO> response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activityList);
        CustomResponseWrapper<List<ActivityOnFilmResponseDTO>> wrapper = CustomResponseWrapper.<List<ActivityOnFilmResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Liked films of user with ID: %s", userId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/on-film")
    public ResponseEntity<CustomResponseWrapper<ActivityOnFilmResponseDTO>> getFilmActivity(@RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId) {
        ActivityOnFilm activity = activityService.getOrCreateActivityById(new ActivityOnFilmId(userId, filmId));
        ActivityOnFilmResponseDTO response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activity);
        CustomResponseWrapper<ActivityOnFilmResponseDTO> wrapper = CustomResponseWrapper.<ActivityOnFilmResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("User with ID %s activity for film with ID: %s", userId, filmId))
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PutMapping("/update-has-review")
    public ResponseEntity<CustomResponseWrapper<ActivityOnFilmResponseDTO>> updateHasReview(
            @RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId) {
        ActivityOnFilm activity = activityService.updateHasReview(userId, filmId);
        ActivityOnFilmResponseDTO response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activity);
        CustomResponseWrapper<ActivityOnFilmResponseDTO> wrapper = CustomResponseWrapper.<ActivityOnFilmResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Has review status has been updated")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PatchMapping("/update-is-liked")
    public ResponseEntity<CustomResponseWrapper<ActivityOnFilmResponseDTO>> updateIsLiked(
            @RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId) {
        ActivityOnFilm activity = activityService.updateIsLiked(userId, filmId);
        ActivityOnFilmResponseDTO response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activity);
        CustomResponseWrapper<ActivityOnFilmResponseDTO> wrapper = CustomResponseWrapper.<ActivityOnFilmResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Like status has been updated")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PatchMapping("/update-rating")
    public ResponseEntity<CustomResponseWrapper<ActivityOnFilmResponseDTO>> updateRating(
            @RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId, @RequestBody Double rating) {
        ActivityOnFilm activity = activityService.updateRating(userId, filmId, rating);
        ActivityOnFilmResponseDTO response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activity);
        CustomResponseWrapper<ActivityOnFilmResponseDTO> wrapper = CustomResponseWrapper.<ActivityOnFilmResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Rating has been updated")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PatchMapping("/remove-rating")
    public ResponseEntity<CustomResponseWrapper<ActivityOnFilmResponseDTO>> removeRating(
            @RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId) {

        ActivityOnFilm activity = activityService.removeRating(userId, filmId);
        ActivityOnFilmResponseDTO response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activity);
        CustomResponseWrapper<ActivityOnFilmResponseDTO> wrapper = CustomResponseWrapper.<ActivityOnFilmResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Rating has been removed")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PatchMapping("/update-is-watched")
    public ResponseEntity<CustomResponseWrapper<ActivityOnFilmResponseDTO>> updateIsWatched(
            @RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId) {

        ActivityOnFilm activity = activityService.updateIsWatched(userId, filmId);
        ActivityOnFilmResponseDTO response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activity);
        CustomResponseWrapper<ActivityOnFilmResponseDTO> wrapper = CustomResponseWrapper.<ActivityOnFilmResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Is watched status has been updated")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PatchMapping("/update-is-in-watchlist")
    public ResponseEntity<CustomResponseWrapper<ActivityOnFilmResponseDTO>> updateIsInWatchlist(
            @RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId) {

        ActivityOnFilm activity = activityService.updateIsInWatchlist(userId, filmId);
        ActivityOnFilmResponseDTO response = activityMapper.mapActivityOnFilmToActivityOnFilmResponse(activity);
        CustomResponseWrapper<ActivityOnFilmResponseDTO> wrapper = CustomResponseWrapper.<ActivityOnFilmResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Is in watchlist status has been updated")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }
}
