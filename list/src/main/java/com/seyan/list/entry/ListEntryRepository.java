package com.seyan.list.entry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ListEntryRepository extends JpaRepository<ListEntry, ListEntryId> {
    List<ListEntry> findByListId(Long listId);

    @Modifying
    @Query(value = "delete from entries where list_id = :listId", nativeQuery = true)
    void deleteEntriesByListId(@Param("listId") Long listId);

    void deleteByFilmIdIn(List<Long> ids);
}
