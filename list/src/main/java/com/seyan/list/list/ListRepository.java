package com.seyan.list.list;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ListRepository extends JpaRepository<List, Long> {
    java.util.List<List> findByUserId(Long id);

    Page<List> findByUserId(Long id, Pageable pageable);

    Page<List> findByUsername(String username, Pageable pageable);

    Optional<List> findByTitle(String title);
}
