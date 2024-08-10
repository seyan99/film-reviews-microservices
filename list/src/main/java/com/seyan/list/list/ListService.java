package com.seyan.list.list;

import com.seyan.list.external.comment.CommentClient;
import com.seyan.list.external.comment.CommentResponseDTO;
import com.seyan.list.dto.*;
import com.seyan.list.entry.ListEntry;
import com.seyan.list.entry.ListEntryRepository;
import com.seyan.list.exception.ListNotFoundException;
import com.seyan.list.external.comment.PageableCommentResponseDTO;
import com.seyan.list.external.film.FilmClient;
import com.seyan.list.external.film.FilmPreviewResponseDTO;
import com.seyan.list.external.film.PageableFilmPreviewResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class ListService {
    private final ListRepository listRepository;
    private final ListMapper listMapper;
    private final ListEntryRepository entryRepository;
    private final FilmClient filmClient;
    private final CommentClient commentClient;

    public java.util.List<CommentResponseDTO> getLatestComments(Long postId) {
        return commentClient.getLatestByPost(postId, "LIST").getData();
    }

    public List addListComment(Long listId, Long commentId) {
        List list = listRepository.findById(listId).orElseThrow(() -> new ListNotFoundException(
                String.format("No list found with the provided ID: %s", listId)
        ));

        list.getCommentIds().add(commentId);
        return listRepository.save(list);
    }

    public List deleteListComment(Long listId, Long commentId) {
        List list = listRepository.findById(listId).orElseThrow(() -> new ListNotFoundException(
                String.format("No list found with the provided ID: %s", listId)
        ));

        list.getCommentIds().remove(commentId);
        return listRepository.save(list);
    }

    public List updateListLikes(Long listId, Long userId) {
        List list = listRepository.findById(listId).orElseThrow(() -> new ListNotFoundException(
                String.format("No list found with the provided ID: %s", listId)
        ));

        if (!list.getLikedUsersIds().contains(userId)) {
            list.getLikedUsersIds().add(userId);
        } else {
            list.getLikedUsersIds().remove(userId);
        }
        return listRepository.save(list);
    }

    @Transactional
    public List createList(ListCreationDTO dto) {
        LocalDateTime creationDate = LocalDateTime.now();

        List mapped = listMapper.mapListCreationDTOToList(dto);
        mapped.setCreationDate(creationDate);
        mapped.setLastUpdateDate(creationDate);
        List withId = listRepository.save(mapped);

        java.util.List<ListEntry> entries = mapListUniqueEntries(withId.getId(), dto.filmIds(), creationDate);
        java.util.List<ListEntry> entriesWithOrder = entries.stream().peek(it -> it.setEntryOrder((long) entries.indexOf(it))).toList();

        entryRepository.saveAll(entriesWithOrder);

        return withId;
    }

    private java.util.List<ListEntry> mapListUniqueEntries(Long listId, java.util.List<Long> filmIds, LocalDateTime addDate) {
        java.util.List<Long> filmIdsUnique = filmIds.stream().distinct().toList();
        return listMapper.mapFilmIdsToListEntries(listId, filmIdsUnique, addDate);
    }

    @Transactional
    public List updateFilmList(ListUpdateDTO dto, Long id) {
        List list = listRepository.findById(id).orElseThrow(() -> new ListNotFoundException(
                String.format("Cannot update film list:: No list found with the provided ID: %s", id)
        ));

        LocalDateTime updateDate = LocalDateTime.now();
        List mapped = listMapper.mapListUpdateDTOToList(dto, list);
        mapped.setLastUpdateDate(updateDate);

        if (dto.filmIds() != null) {
            java.util.List<Long> filmIdsUpdate = dto.filmIds().stream().distinct().toList();

            Map<Boolean, java.util.List<ListEntry>> existingEntries = entryRepository.findByListId(list.getId()).stream()
                    .collect(Collectors.partitioningBy(it -> filmIdsUpdate.contains(it.getFilmId())));

            java.util.List<ListEntry> existingToLeftEntries = existingEntries.get(true).stream().filter(it -> filmIdsUpdate.contains(it.getFilmId())).toList();
            java.util.List<Long> existingToLeftFilmIds = existingToLeftEntries.stream().map(ListEntry::getFilmId).toList();

            java.util.List<Long> newFilmIds = filmIdsUpdate.stream().filter(it -> !existingToLeftFilmIds.contains(it)).toList();
            java.util.List<ListEntry> newEntries = mapListUniqueEntries(list.getId(), newFilmIds, updateDate);

            java.util.List<ListEntry> updatedEntries = Stream.concat(existingToLeftEntries.stream(), newEntries.stream())
                    .peek(it -> it.setEntryOrder((long) filmIdsUpdate.indexOf(it.getFilmId())))
                    .sorted(Comparator.comparing(ListEntry::getEntryOrder)).toList();

            java.util.List<Long> entryIdsToDelete = existingEntries.get(false).stream().map(ListEntry::getFilmId).toList();

            if (!entryIdsToDelete.isEmpty()) {
                entryRepository.deleteByFilmIdIn(entryIdsToDelete);
            }

            entryRepository.saveAll(updatedEntries);
        }
        return listRepository.save(mapped);
    }

    public List getFilmListById(Long id) {
        return listRepository.findById(id).orElseThrow(() -> new ListNotFoundException(
                String.format("No list found with the provided ID: %s", id)
        ));
    }

    public List getListByTitle(String title) {
        String[] split = title.split("-");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < split.length - 1; i++) {
            builder.append(split[i]).append(" ");
        }
        String parsedTitle = builder.append(split[split.length - 1]).toString();
        return listRepository.findByTitle(title).orElseThrow(() -> new ListNotFoundException(
                String.format("No list found with the provided title: %s", parsedTitle)
        ));
    }

    public java.util.List<List> getAllFilmLists() {
        return listRepository.findAll();
    }

    public Page<List> getAllFilmLists(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        return listRepository.findAll(pageable);
    }

    public java.util.List<List> getFilmListsByUserId(Long userId) {
        return listRepository.findByUserId(userId);
    }

    public Page<List> getFilmListsByUserId(Long userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return listRepository.findByUserId(userId, pageable);
    }

    public Page<List> getFilmListsByUsername(String username, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        return listRepository.findByUsername(username, pageable);
    }

    @Transactional
    public void deleteFilmList(Long id) {
        listRepository.findById(id).orElseThrow(() -> new ListNotFoundException(
                String.format("Cannot delete film list:: No list found with the provided ID: %s", id)
        ));

        entryRepository.deleteEntriesByListId(id);
        listRepository.deleteById(id);
    }

    public java.util.List<FilmPreviewResponseDTO> getFilmsFromListById(java.util.List<Long> filmIds) {
        java.util.List<FilmPreviewResponseDTO> films = filmClient.getFilmsByIdList(filmIds).getData();

        films.sort(Comparator.comparing(it -> filmIds.indexOf(it.id())));

        return films;
    }

    public PageableFilmPreviewResponseDTO getFilmsFromList(java.util.List<ListEntry> entries, int pageNo) {
        java.util.List<Long> filmIds = entries.stream().sorted(Comparator.comparing(ListEntry::getEntryOrder)).map(ListEntry::getFilmId).toList();
        return filmClient.getFilmsFromList(pageNo, filmIds).getData();
    }

    public PageableCommentResponseDTO getCommentsFromList(Long listId, int pageNo) {
        return commentClient.getAllByList(listId, pageNo).getData();
    }
}
