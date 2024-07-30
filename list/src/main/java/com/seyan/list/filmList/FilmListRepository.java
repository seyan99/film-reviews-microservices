package com.seyan.list.filmList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FilmListRepository extends JpaRepository<FilmList, Long> {
    List<FilmList> findByUserId(Long id);
    //@Query(value = "DELETE fc,p FROM films_cast fc INNER JOIN profiles p ON fc.cast_id = p.id WHERE id = :id", nativeQuery = true)
    //void deleteWithRelation(@Param("id") Long id);
    //Query(value = "DELETE le,e FROM list_entries le INNER JOIN entries e ON le.entries_id = e.id WHERE film_list_id = :listId", nativeQuery = true)

    @Modifying
    @Query(value = "DELETE FROM entries WHERE list_id = :listId", nativeQuery = true)
    void clearEntriesBeforeUpdate(@Param("listId") Long listId);

    @Modifying
    @Query(value = "DELETE FROM lists_entries WHERE entries_list_id = :listId", nativeQuery = true)
    void clearListEntriesRelationBeforeUpdate(@Param("listId") Long listId);

    /*@Query("DELETE rsh,rs from RoamingStatusHistory rsh inner join RoamingStatus rs on rsh.msisdn = rs.msisdn where TIMEDIFF(NOW(),rsh.createdDate)>'00:00:30'",
            nativeQuery = true)*/


}
