package com.seyan.film.film;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    //List<Film> findByGenre(Genre genre);

    //todo fix
    //List<Film> findByDecade(String decade);  THERE IS NO DECADE IN FILM ENTITY

    //List<Film> findByDecadeAndGenre(String decade, Genre genre);

    Optional<Film> findByUrl(String filmUrl);




    //////////////////////////////////////////////////

    //Film Name
    //List<Film> findAllByOrderByTitleAsc();
    //todo debug search with similar titles
    //List<Film> findByOrderByTitleAsc();

    //Release Date
    //List<Film> findByOrderByReleaseDateDesc(); //Newest First
    //List<Film> findByOrderByReleaseDateAsc(); //Earliest First

    //Average Rating
    //List<Film> findByOrderByRatingDesc(); //Highest First
    //List<Film> findByOrderByRatingAsc(); //Lowest First

    //Your Rating
    //findByInventoryIdIn(List<Long> inventoryIdList);

    //List<Film> findByOrderByIdAndRatingDescIn(List<Long> films); //Highest First
   // List<Film> findByIdByOrderByRatingDescIn(List<Long> films); //Highest First
    //List<Film> findByOrderByIdAndRatingAscIn(List<Long> films); //Lowest First

//    Your Interests
//    Based on films you liked
//    Related to films you liked
//    Film Length
//    Shortest First
//    Longest First
//    Film Popularity
//    All Time
//    This Week
//    This Month
//    This Year
//    Film Popularity with Friends
//    All Time
//    This Week
//    This Month
//    This Year
    //Optional<Film> findByTitle(String title);

    Optional<Film> findByTitle(String title);

    List<Film> findAllByTitle(String title);

    List<Film> findByReleaseDateBetween(LocalDate rangeFrom, LocalDate rangeTo);

    Optional<Film> findByReleaseDate(LocalDate release);

    List<Film> findByTitleContaining(String title);

    int countByUrlContaining(String url);
    int countByTitleIgnoreCase(String title);

    //List<Film> findByRatingDesc(Double rating);

    //List<Film> findByRatingAsc(Double rating);

    //findAllByOrderByIdAsc

    //findByBooOrderById

    //findByUsernameInOrEmailIn

    //findByTitleContainingIgnoreCase
    //todo clean up all this
}
