package com.seyan.list.filmList;

import com.seyan.reviewmonolith.comment.CommentService;
import com.seyan.reviewmonolith.exception.filmList.FilmListNotFoundException;
import com.seyan.reviewmonolith.film.Film;
import com.seyan.reviewmonolith.film.FilmService;
import com.seyan.reviewmonolith.filmList.dto.FilmInFilmListResponseDTO;
import com.seyan.reviewmonolith.filmList.dto.FilmListCreationDTO;
import com.seyan.reviewmonolith.filmList.dto.FilmListMapper;
import com.seyan.reviewmonolith.filmList.dto.FilmListUpdateDTO;
import com.seyan.reviewmonolith.filmList.entry.ListEntry;
import com.seyan.reviewmonolith.filmList.entry.ListEntryRepository;
import lombok.RequiredArgsConstructor;
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

    //todo replace with controller calls
    private final FilmService filmService;
    private final CommentService commentService;

    public void addListComment(Long listId, Long commentId) {
        //todo controller methods
        FilmList list = filmListRepository.findById(listId).orElseThrow(() -> new FilmListNotFoundException(
                String.format("No list found with the provided ID: %s", listId)
        ));

        list.getCommentIds().add(commentId);
        filmListRepository.save(list);
    }

    public void deleteListComment(Long listId, Long commentId) {
        FilmList list = filmListRepository.findById(listId).orElseThrow(() -> new FilmListNotFoundException(
                String.format("No list found with the provided ID: %s", listId)
        ));

        list.getCommentIds().remove(commentId);
        filmListRepository.save(list);
    }

    public FilmList updateListLikes(Long listId, Long userId) {
        FilmList list = filmListRepository.findById(listId).orElseThrow(() -> new FilmListNotFoundException(
                String.format("No list found with the provided ID: %s", listId)
        ));

        if (!list.getLikedUsersIds().contains(userId)) {
            list.getLikedUsersIds().add(userId);
            //list.setLikeCount(list.getLikeCount() + 1);
        } else {
            list.getLikedUsersIds().remove(userId);
            //list.setLikeCount(list.getLikeCount() - 1);
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

    //todo add checks for if present and unique/ordering
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
            List<Long> existingToLeftFilmIds = existingToLeftEntries.stream().map(it -> it.getFilmId()).toList();

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

    //todo add you watched % of list
    //todo Popularity All Time This Week This Month This Year

    public List<FilmList> getAllFilmLists() {
        return filmListRepository.findAll();
    }

    public List<FilmList> getAllFilmListsByUserId(Long userId) {
        return filmListRepository.findByUserId(userId);
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
        List<Film> films = filmService.getFilmsByIdList(filmIds);

        films.sort(Comparator.comparing(it -> filmIds.indexOf(it.getId())));

        return filmListMapper.mapFilmToFilmInFilmListResponseDTO(films);
    }
}
