package com.seyan.film.film;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.seyan.film.profile.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "films")
@DynamicUpdate
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private LocalDate releaseDate;

    @Column(unique = true)
    private String url;

    private Double avgRating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_id")
    private Profile director;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "film_cast",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id"))
    private Set<Profile> cast;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private Integer runningTimeMinutes;

    private Long watchedCount;

    private Long listCount;

    private Long likeCount;

    public Film() {
        this.avgRating = 0.0;
        this.cast = new HashSet<>();
        this.watchedCount = 0L;
        this.listCount = 0L;
        this.likeCount = 0L;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                '}';
    }
}
