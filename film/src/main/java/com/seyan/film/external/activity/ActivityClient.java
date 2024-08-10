package com.seyan.film.external.activity;


import com.seyan.film.responsewrapper.CustomResponseWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "activity-service",
        url = "${application.config.activity-url}"
)
public interface ActivityClient {

    @GetMapping("/by-user-id-and-rating")
    public CustomResponseWrapper<List<ActivityOnFilmResponseDTO>> getActivityByUserIdAndByRatingGreaterThan(
            @RequestParam("userId") Long userId,
            @RequestParam("rating") Double rating);

    @GetMapping("/on-film")
    CustomResponseWrapper<ActivityOnFilmResponseDTO> getFilmActivity(@RequestParam("userId") Long userId, @RequestParam("filmId") Long filmId);
}
