package com.seyan.review.dto;


import com.seyan.review.review.Review;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ReviewMapper {
    public PageableReviewResponseDTO mapReviewsPageToPageableReviewResponseDTO(Page<Review> comments) {
        List<ReviewResponseDTO> mapped = mapReviewToReviewResponseDTO(comments.getContent());

        return PageableReviewResponseDTO.builder()
                .content(mapped)
                .pageNo(comments.getNumber())
                .pageSize(comments.getSize())
                .totalElements(comments.getTotalElements())
                .totalPages(comments.getTotalPages())
                .last(comments.isLast())
                .build();
    }

    public Review mapReviewCreationDTOToReview(ReviewCreationDTO dto) {
        Review review = new Review();
        BeanUtils.copyProperties(dto, review, getNullFieldNames(dto));
        return review;
    }

    public Review mapReviewUpdateDTOToReview(ReviewUpdateDTO source, Review destination) {
        BeanUtils.copyProperties(source, destination, getNullFieldNames(source));
        if (source.watchedOnDate() == null) {
            destination.setWatchedOnDate(null);
        }
        return destination;
    }

    public ReviewResponseDTO mapReviewToReviewResponseDTO(Review review) {
        return new ReviewResponseDTO(
                review.getId(),
                review.getRating(),
                review.getIsLiked(),
                review.getContent(),
                review.getWatchedOnDate(),
                review.getContainsSpoilers(),
                review.getCreationDate(),
                review.getFilmId(),
                review.getTitle(),
                review.getUserId(),
                review.getUsername(),
                review.getLikedCount(),
                review.getCommentIds().size()
        );
    }

    public List<ReviewResponseDTO> mapReviewToReviewResponseDTO(List<Review> reviews) {
        if (reviews == null) {
            return null;
        }

        return reviews.stream()
                .map(this::mapReviewToReviewResponseDTO)
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
}
