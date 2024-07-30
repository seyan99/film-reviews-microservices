package com.seyan.review.review;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
//@NoArgsConstructor
@Builder
@Entity
@Table(name = "reviews")
//@DynamicUpdate
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double rating;
    private Boolean isLiked;
    private String content;
    private Boolean containsSpoilers;
    private LocalDate creationDate;
    private Long filmId;
    private Long userId;

    //private Long likeCount;
    private Set<Long> likedUsersIds;

    //private Long commentCount;
    private Set<Long> commentIds;

    //todo this fields adds film to your diary
    private LocalDate watchedOnDate;
    private Boolean watchedThisFilmBefore;


    //private Long reviewLikeCount;

    //private Long commentCount;

    //todo counts only if has content


    //TODO boolean flags + user service get methods

    public Review() {
        this.creationDate = LocalDate.now();
        this.likedUsersIds = new HashSet<>();
        this.commentIds = new HashSet<>();
    }
}
