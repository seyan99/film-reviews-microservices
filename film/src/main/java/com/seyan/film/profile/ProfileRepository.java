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
    //@Query(value = "DELETE fc,p FROM films_cast fc INNER JOIN profiles p ON fc.cast_id = p.id WHERE id = :id", nativeQuery = true)
    //void deleteWithRelation(@Param("id") Long id);

    @Modifying
    @Query(value = "delete from film_cast where profile_id = :id", nativeQuery = true)
    void deleteRelation(@Param("id") Long id);

    List<Profile> findByNameContaining(String url);

    @Query(value = "insert into film_cast (id,name) VALUES (:profileId,:filmId)", nativeQuery = true)
    void addFilmProfileRelation(@Param("profileId") Long profileId, @Param("filmId") Long filmId);

    @Query(value = "delete from film_cast where film_id = :filmId and profile_id = :profileId)", nativeQuery = true)
    void removeFilmProfileRelation(@Param("profileId") Long profileId, @Param("filmId") Long filmId);

    Optional<Profile> findByUrl(String url);

    int countByUrlContaining(String url);

    int countByNameContaining(String name);
    int countByNameIgnoreCase(String name);
}
