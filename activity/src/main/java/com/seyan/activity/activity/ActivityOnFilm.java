package com.seyan.activity.activity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "activity_on_film")
@DynamicUpdate
public class ActivityOnFilm {
    /*@Id
    private Long userId;
    @Id
    private Long filmId;*/
    @EmbeddedId
    private ActivityOnFilmId id;
    private Boolean isWatched;
    private Boolean isLiked;
    private Boolean isInWatchlist;
    private Double rating;
    private LocalDate watchlistAddDate;

    private Boolean hasReview;
    //private Long lastReviewId;
    //@OneToMany(fetch = FetchType.LAZY)
    //private List<Long> filmReviewIds;

    //todo relation
    //@OneToOne
    //private Review review;
}
