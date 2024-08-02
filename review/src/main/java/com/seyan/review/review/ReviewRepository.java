package com.seyan.review.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    int countByUserIdAndFilmId(Long userId, Long filmId);

    List<Review> findByFilmIdAndContentNotNull(Long filmId);

    @Query(value = "select film_id from reviews where creation_date >= :date", nativeQuery = true)
    List<Long> findFilmIdBasedOnReviewCreationDateAfter(@Param("date") LocalDate date);

    @Query(value = "select film_id from reviews where creation_date <= :date", nativeQuery = true)
    List<Long> findFilmIdBasedOnReviewCreationDateBefore(@Param("date") LocalDate date);

    @Query(value = "select film_id from reviews", nativeQuery = true)
    List<Long> findAllFilmIds();

    List<Review> findByUserIdAndContentNotNull(Long userId);

    List<Review> findByUserIdAndWatchedOnDateNotNull(Long userId);

    List<Review> findByUserIdAndFilmIdAndContentNotNull(Long userId, Long filmId);

    int countByUserIdAndFilmIdAndContentNotNull(Long userId, Long filmId);
}
