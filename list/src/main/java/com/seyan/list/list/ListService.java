package com.seyan.list.list;

import com.seyan.list.exception.ListParametersException;
import com.seyan.list.exception.SortingParametersException;
import com.seyan.list.external.comment.CommentClient;
import com.seyan.list.external.comment.CommentResponseDTO;
import com.seyan.list.dto.*;
import com.seyan.list.entry.ListEntry;
import com.seyan.list.entry.ListEntryRepository;
import com.seyan.list.exception.ListNotFoundException;
import com.seyan.list.external.comment.PageableCommentResponseDTO;
import com.seyan.list.external.film.FilmClient;
import com.seyan.list.external.film.FilmPreviewResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
    private final ListSearchDao listSearchDao;

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
        if (listRepository.countByTitleAndUsername(dto.title(), dto.username()) > 0) {
            throw new ListParametersException(
                    String.format("List with the provided title \"%s\" and username \"%s\" already exists", dto.title(), dto.username()));
        }

        List mapped = listMapper.mapListCreationDTOToList(dto);
        List withId = listRepository.save(mapped);

        java.util.List<ListEntry> entries = mapListUniqueEntries(withId.getId(), dto.filmIds(), dto.creationDate());
        java.util.List<ListEntry> entriesWithOrder = entries.stream().peek(it -> it.setEntryOrder((long) entries.indexOf(it))).toList();

        entryRepository.saveAll(entriesWithOrder);

        return withId;
    }

    private java.util.List<ListEntry> mapListUniqueEntries(Long listId, java.util.List<Long> filmIds, LocalDateTime addDate) {
        java.util.List<Long> filmIdsUnique = filmIds.stream().distinct().toList();
        return listMapper.mapFilmIdsToListEntries(listId, filmIdsUnique, addDate);
    }

    @Transactional
    public List updateList(ListUpdateDTO dto, Long id) {
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

    public List getListById(Long id) {
        return listRepository.findById(id).orElseThrow(() -> new ListNotFoundException(
                String.format("No list found with the provided ID: %s", id)
        ));
    }

    public Page<List> getAllLists(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        return listRepository.findAll(pageable);
    }

    public Page<List> getAllListsWithSorting(String sorting, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        return listSearchDao.findByUsernameAndSorting(null, sorting, pageable);
    }

    public List getListByTitle(String title) {
        String parsedTitle = parseTitle(title);
        System.out.println("parsedTitle = " + parsedTitle);
        return listRepository.findByTitleContaining(title).orElseThrow(() -> new ListNotFoundException(
                String.format("No list found with the provided title: %s", parsedTitle)
        ));
    }

    public Page<List> getListsByUsername(String username, String sorting, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        return listSearchDao.findByUsernameAndSorting(username, sorting, pageable);
        //return listRepository.findByUsername(username, pageable);
    }

    public List getListByTitleAndUsername(String title, String username) {
        String parsedTitle = parseTitle(title);
        return listRepository.findByTitleAndUsername(title, username).orElseThrow(() -> new ListNotFoundException(
                String.format("No list found with the provided title: %s and username: %s", parsedTitle, username)));
    }

    private String parseTitle(String title) {
        String[] split = title.split("-");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < split.length - 1; i++) {
            builder.append(split[i]).append(" ");
        }

        System.out.println("builder.append(split[split.length - 1]).toString() = " + "A" + builder.append(split[split.length - 1]) + "A");
        return builder.toString();
    }

    public java.util.List<List> getAllLists() {
        return listRepository.findAll();
    }

    public java.util.List<List> getListsByUserId(Long userId) {
        return listRepository.findByUserId(userId);
    }

    public Page<List> getListsByUserId(Long userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return listRepository.findByUserId(userId, pageable);
    }

    @Transactional
    public void deleteList(Long id) {
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

    public java.util.List<FilmPreviewResponseDTO> getFilmsFromList(java.util.List<ListEntry> entries, String filmsOrder, int pageNo) {
        int pageStartElement = 0;
        int pageEndElement;


        if (entries.size() <= 5) {
            pageEndElement = entries.size();
        } else if (pageNo == 1) {
            pageEndElement = 5;
        } else if (pageNo > 1 && entries.size() > pageNo * 5) {
            pageEndElement = pageNo * 5;
            pageStartElement = pageNo * 5 - 5;
        } else if (pageNo > 1 && entries.size() < pageNo * 5 && pageNo <= entries.size() / pageNo) {
            pageStartElement = pageNo * 5 - 5;
            pageEndElement = entries.size();
        } else {
            return Collections.emptyList();
        }

        java.util.List<Long> filmIds = sortEntriesToIds(entries, filmsOrder)
                .subList(pageStartElement, pageEndElement);

        return filmClient.getFilmsFromList(filmIds).getData();
    }

    private java.util.List<Long> sortEntriesToIds(java.util.List<ListEntry> entries, String sorting) {
        switch (sorting) {
            case "list-order" -> {
                return entries.stream().sorted(Comparator.comparing(ListEntry::getEntryOrder)).map(ListEntry::getFilmId).toList();
            }
            case "reverse-order" -> {
                return entries.stream().sorted(Comparator.comparing(ListEntry::getEntryOrder).reversed()).map(ListEntry::getFilmId).toList();
            }
            case "added-newest" -> {
                return entries.stream().sorted(Comparator.comparing(ListEntry::getWhenAdded)).map(ListEntry::getFilmId).toList();
            }
            case "added-earliest" -> {
                return entries.stream().sorted(Comparator.comparing(ListEntry::getWhenAdded).reversed()).map(ListEntry::getFilmId).toList();
            }
            default -> throw new SortingParametersException(
                    "Could not parse sorting parameter");
        }
    }

    public PageableCommentResponseDTO getCommentsFromList(Long listId, int pageNo) {
        return commentClient.getAllByList(listId, pageNo).getData();
    }
}
