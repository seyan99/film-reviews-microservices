package com.seyan.film.film;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    Optional<Film> findByUrl(String filmUrl);

    List<Film> findByTitleContaining(String title);

    int countByTitleIgnoreCase(String title);
}
