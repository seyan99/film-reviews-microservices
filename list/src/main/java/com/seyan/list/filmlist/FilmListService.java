package com.seyan.list.filmlist;

import com.seyan.list.comment.CommentClient;
import com.seyan.list.comment.CommentResponseDTO;
import com.seyan.list.dto.FilmInFilmListResponseDTO;
import com.seyan.list.dto.FilmListCreationDTO;
import com.seyan.list.dto.FilmListMapper;
import com.seyan.list.dto.FilmListUpdateDTO;
import com.seyan.list.entry.ListEntry;
import com.seyan.list.entry.ListEntryRepository;
import com.seyan.list.exception.FilmListNotFoundException;
import com.seyan.list.film.FilmClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class FilmListService {
    private final FilmListRepository filmListRepository;
    private final FilmListMapper filmListMapper;
    private final ListEntryRepository entryRepository;
    private final FilmClient filmClient;
    private final CommentClient commentClient;

    public List<CommentResponseDTO> getLatestComments(Long postId) {
        return commentClient.getLatestByPost(postId, "LIST").getData();
    }

    public FilmList addListComment(Long listId, Long commentId) {
        FilmList list = filmListRepository.findById(listId).orElseThrow(() -> new FilmListNotFoundException(
                String.format("No list found with the provided ID: %s", listId)
        ));

        list.getCommentIds().add(commentId);
        return filmListRepository.save(list);
    }

    public FilmList deleteListComment(Long listId, Long commentId) {
        FilmList list = filmListRepository.findById(listId).orElseThrow(() -> new FilmListNotFoundException(
                String.format("No list found with the provided ID: %s", listId)
        ));

        list.getCommentIds().remove(commentId);
        return filmListRepository.save(list);
    }

    public FilmList updateListLikes(Long listId, Long userId) {
        FilmList list = filmListRepository.findById(listId).orElseThrow(() -> new FilmListNotFoundException(
                String.format("No list found with the provided ID: %s", listId)
        ));

        if (!list.getLikedUsersIds().contains(userId)) {
            list.getLikedUsersIds().add(userId);
        } else {
            list.getLikedUsersIds().remove(userId);
        }
        return filmListRepository.save(list);
    }


    @Transactional
    public FilmList createList(FilmListCreationDTO dto) {
        LocalDateTime creationDate = LocalDateTime.now();

        FilmList mapped = filmListMapper.mapFilmListCreationDTOToFilmList(dto);
        FilmList withId = filmListRepository.save(mapped);

        List<ListEntry> entries = mapListUniqueEntries(withId.getId(), dto.filmIds(), creationDate);
        List<ListEntry> entriesWithOrder = entries.stream().peek(it -> it.setEntryOrder((long) entries.indexOf(it))).toList();

        entryRepository.saveAll(entriesWithOrder);

        withId.setCreationDate(creationDate);
        withId.setLastUpdateDate(creationDate);
        return filmListRepository.save(withId);
    }

    private List<ListEntry> mapListUniqueEntries(Long listId, List<Long> filmIds, LocalDateTime addDate) {
        List<Long> filmIdsUnique = filmIds.stream().distinct().toList();
        return filmListMapper.mapFilmIdsToListEntries(listId, filmIdsUnique, addDate);
    }

    @Transactional
    public FilmList updateFilmList(FilmListUpdateDTO dto, Long id) {
        FilmList list = filmListRepository.findById(id).orElseThrow(() -> new FilmListNotFoundException(
                String.format("Cannot update film list:: No list found with the provided ID: %s", id)
        ));

        LocalDateTime updateDate = LocalDateTime.now();
        FilmList mapped = filmListMapper.mapFlmListUpdateDTOToFilmList(dto, list);
        mapped.setLastUpdateDate(updateDate);

        if (dto.filmIds() != null) {
            List<Long> filmIdsUpdate = dto.filmIds().stream().distinct().toList();

            Map<Boolean, List<ListEntry>> existingEntries = entryRepository.findByListId(list.getId()).stream()
                    .collect(Collectors.partitioningBy(it -> filmIdsUpdate.contains(it.getFilmId())));

            List<ListEntry> existingToLeftEntries = existingEntries.get(true).stream().filter(it -> filmIdsUpdate.contains(it.getFilmId())).toList();
            List<Long> existingToLeftFilmIds = existingToLeftEntries.stream().map(ListEntry::getFilmId).toList();

            List<Long> newFilmIds = filmIdsUpdate.stream().filter(it -> !existingToLeftFilmIds.contains(it)).toList();
            List<ListEntry> newEntries = mapListUniqueEntries(list.getId(), newFilmIds, updateDate);

            List<ListEntry> updatedEntries = Stream.concat(existingToLeftEntries.stream(), newEntries.stream())
                    .peek(it -> it.setEntryOrder((long) filmIdsUpdate.indexOf(it.getFilmId())))
                    .sorted(Comparator.comparing(ListEntry::getEntryOrder)).toList();

            List<Long> entryIdsToDelete = existingEntries.get(false).stream().map(ListEntry::getFilmId).toList();

            if (!entryIdsToDelete.isEmpty()) {
                entryRepository.deleteByFilmIdIn(entryIdsToDelete);
            }

            entryRepository.saveAll(updatedEntries);
        }
        return filmListRepository.save(mapped);
    }

    public FilmList getFilmListById(Long id) {
        return filmListRepository.findById(id).orElseThrow(() -> new FilmListNotFoundException(
                String.format("No list found with the provided ID: %s", id)
        ));
    }

    public List<FilmList> getAllFilmLists() {
        return filmListRepository.findAll();
    }

    public Page<FilmList> getAllFilmLists(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return filmListRepository.findAll(pageable);
    }

    public List<FilmList> getAllFilmListsByUserId(Long userId) {
        return filmListRepository.findByUserId(userId);
    }

    public Page<FilmList> getAllFilmListsByUserId(Long userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return filmListRepository.findByUserId(userId, pageable);
    }

    @Transactional
    public void deleteFilmList(Long id) {
        filmListRepository.findById(id).orElseThrow(() -> new FilmListNotFoundException(
                String.format("Cannot delete film list:: No list found with the provided ID: %s", id)
        ));

        entryRepository.deleteEntriesByListId(id);
        filmListRepository.deleteById(id);
    }

    public List<FilmInFilmListResponseDTO> getFilmsFromList(List<Long> filmIds) {
        List<FilmInFilmListResponseDTO> films = filmClient.getFilmsByIdList(filmIds).getData();

        films.sort(Comparator.comparing(it -> filmIds.indexOf(it.id())));

        return films;
    }
}
