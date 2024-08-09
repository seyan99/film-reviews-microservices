package com.seyan.review.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    Page<Review> findByFilmIdAndContentNotNull(Long filmId, Pageable pageable);

    @Query(value = "select film_id from reviews where creation_date >= :date", nativeQuery = true)
    List<Long> findFilmIdBasedOnReviewCreationDateAfter(@Param("date") LocalDate date);

    @Query(value = "select film_id from reviews where creation_date <= :date", nativeQuery = true)
    List<Long> findFilmIdBasedOnReviewCreationDateBefore(@Param("date") LocalDate date);

    @Query(value = "select film_id from reviews", nativeQuery = true)
    List<Long> findAllFilmIds();

    List<Review> findByUserIdAndContentNotNull(Long userId);

    Page<Review> findByUserIdAndContentNotNull(Long userId, Pageable pageable);

    List<Review> findByUserIdAndWatchedOnDateNotNull(Long userId);

    Page<Review> findByUserIdAndWatchedOnDateNotNull(Long userId, Pageable pageable);

    Page<Review> findByUsernameAndWatchedOnDateNotNull(String username, Pageable pageable);

    List<Review> findByUserIdAndFilmIdAndContentNotNull(Long userId, Long filmId);

    Page<Review> findByUserIdAndFilmIdAndContentNotNull(Long userId, Long filmId, Pageable pageable);

    int countByUserIdAndFilmIdAndContentNotNull(Long userId, Long filmId);

    List<Review> findByFilmIdTop3ByOrderByCreationDateDesc(Long filmId);

    List<Review> findByFilmIdTop3ByLikedUsersIdsDesc(Long filmId);

    Page<Review> findByFilmTitleAndContentNotNull(String title, Pageable pageable);

    Page<Review> findByUsernameAndContentNotNull(String username, Pageable pageable);

    List<Review> findByUsernameAndTitleAndContentNotNull(String username, String title, Sort sort);

    //List<Review> findTop3Latest(Long filmId);

    //List<Review> findTop3Popular(Long filmId, Sort sort);


    /*List<Review> findByFilmIdTop3ByOrderByCreationDateDesc(Long filmId);

    @Query(
            value = "select p from Person p join p.addresses ad group by p Order By addressCount desc",
            countQuery = "select count(p) from Person p"
    )

    @Query(value = "select r from reviews r join r.id ad group by p Order By addressCount desc",
            countQuery = "select count(p) from Person p")
    List<Review> findByFilmIdTop3ByOrderByRatingDesc(Long filmId);*/
}
