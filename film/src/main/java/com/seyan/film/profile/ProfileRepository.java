package com.seyan.film.profile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Modifying
    @Query(value = "delete from film_cast where profile_id = :id", nativeQuery = true)
    void deleteRelation(@Param("id") Long id);

    List<Profile> findByNameContaining(String url);

    Optional<Profile> findByUrl(String url);

    int countByNameIgnoreCase(String name);
}
