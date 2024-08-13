package com.seyan.activity.activity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
public class Activity {
    @EmbeddedId
    private ActivityId id;
    private Boolean isWatched;
    private Boolean isLiked;
    private Boolean isInWatchlist;
    private Double rating;
    private LocalDate watchlistAddDate;
    private Boolean hasReview;
}
