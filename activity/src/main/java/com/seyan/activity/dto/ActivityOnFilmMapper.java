package com.seyan.activity.dto;


import com.seyan.activity.activity.ActivityOnFilm;
import com.seyan.activity.activity.ActivityOnFilmId;
import com.seyan.activity.review.ReviewCreationDTO;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ActivityOnFilmMapper {
    public ActivityOnFilmResponseDTO mapActivityOnFilmToActivityOnFilmResponse(ActivityOnFilm activity) {
        return new ActivityOnFilmResponseDTO(
                activity.getId(),
                activity.getIsWatched(),
                activity.getIsLiked(),
                activity.getIsInWatchlist(),
                activity.getRating(),
                activity.getWatchlistAddDate(),
                activity.getHasReview()
        );
    }

    public List<ActivityOnFilmResponseDTO> mapActivityOnFilmToActivityOnFilmResponse(List<ActivityOnFilm> films) {
        if (films == null) {
            return null;
        }

        return films.stream()
                .map(this::mapActivityOnFilmToActivityOnFilmResponse)
                .toList();
    }

    private String[] getNullFieldNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> fieldNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                fieldNames.add(pd.getName());
        }

        String[] result = new String[fieldNames.size()];
        return fieldNames.toArray(result);
    }

    public ActivityOnFilm mapActivityAndReviewCreationDTOToActivityOnFilm(ActivityAndReviewCreationDTO dto) {
        return ActivityOnFilm.builder()
                .id(new ActivityOnFilmId(
                        dto.userId(),
                        dto.filmId()
                ))
                .isWatched(true)
                .isLiked(dto.isLiked())
                .rating(dto.rating())
                .build();
    }

    public ReviewCreationDTO mapActivityAndReviewCreationDTOToReviewCreationDTO(ActivityAndReviewCreationDTO dto) {
        return new ReviewCreationDTO(
                dto.rating(),
                dto.isLiked(),
                dto.reviewContent(),
                dto.containsSpoilers(),
                dto.filmId(),
                dto.userId(),
                dto.watchedOnDate(),
                dto.watchedThisFilmBefore()
        );
    }
}
