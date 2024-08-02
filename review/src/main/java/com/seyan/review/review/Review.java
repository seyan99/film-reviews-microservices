package com.seyan.review.review;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reviews")
@DynamicUpdate
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
    private Set<Long> likedUsersIds;
    private Set<Long> commentIds;
    private LocalDate watchedOnDate;
    private Boolean watchedThisFilmBefore;

    public Review() {
        this.creationDate = LocalDate.now();
        this.likedUsersIds = new HashSet<>();
        this.commentIds = new HashSet<>();
    }
}
