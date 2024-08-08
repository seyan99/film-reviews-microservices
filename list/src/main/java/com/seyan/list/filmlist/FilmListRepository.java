package com.seyan.list.filmlist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilmListRepository extends JpaRepository<FilmList, Long> {
    List<FilmList> findByUserId(Long id);

    Page<FilmList> findByUserId(Long id, Pageable pageable);
}
