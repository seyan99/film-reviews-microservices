package com.seyan.film.film;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    Optional<Film> findByUrl(String filmUrl);

    List<Film> findByTitleContaining(String title);

    Page<Film> findByTitleContaining(String title, Pageable pageable);

    int countByTitleIgnoreCase(String title);

    Long findIdByTitle(String title);

    Page<Film> findAllByIdIn(List<Long> filmIds, Pageable pageable);
}
