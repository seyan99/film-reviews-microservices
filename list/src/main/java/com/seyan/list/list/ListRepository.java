package com.seyan.list.list;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ListRepository extends JpaRepository<List, Long> {
    java.util.List<List> findByUserId(Long id);

    Page<List> findByUserId(Long id, Pageable pageable);

    Page<List> findByUsername(String username, Pageable pageable);

    //@Query(value = "select * from lists where title = 'a 1'", nativeQuery = true)
    Optional<List> findByTitle(String title);

    @Query(value = "select * from lists where title = :title and username = :username", nativeQuery = true)
    Optional<List> findByTitleAndUsername(@Param("title") String title, @Param("username") String username);

    int countByTitleAndUsername(String title, String username);
}
