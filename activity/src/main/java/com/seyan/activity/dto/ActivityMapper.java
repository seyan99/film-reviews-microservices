package com.seyan.activity.dto;


import com.seyan.activity.activity.Activity;
import com.seyan.activity.activity.ActivityId;
import com.seyan.activity.external.review.ReviewCreationDTO;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ActivityMapper {
    public ActivityResponseDTO mapActivityToActivityResponseDTO(Activity activity) {
        return new ActivityResponseDTO(
                activity.getId(),
                activity.getIsWatched(),
                activity.getIsLiked(),
                activity.getIsInWatchlist(),
                activity.getRating(),
                activity.getWatchlistAddDate(),
                activity.getHasReview()
        );
    }

    public List<ActivityResponseDTO> mapActivityToActivityResponseDTO(List<Activity> films) {
        if (films == null) {
            return null;
        }

        return films.stream()
                .map(this::mapActivityToActivityResponseDTO)
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

    public Activity mapActivityAndReviewCreationDTOToActivity(ActivityAndReviewCreationDTO dto) {
        return Activity.builder()
                .id(new ActivityId(
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
