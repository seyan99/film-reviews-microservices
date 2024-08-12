package com.seyan.list.list;

import com.seyan.list.external.comment.CommentResponseDTO;
import com.seyan.list.dto.*;
import com.seyan.list.entry.ListEntry;
import com.seyan.list.external.comment.PageableCommentResponseDTO;
import com.seyan.list.external.film.FilmPreviewResponseDTO;
import com.seyan.list.external.film.PageableFilmPreviewResponseDTO;
import com.seyan.list.responsewrapper.CustomResponseWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/api/v1/lists")
@RestController
public class ListController {
    private final ListService listService;
    private final ListMapper listMapper;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CustomResponseWrapper<ListResponseDTO>> createFilmList(@RequestBody @Valid ListCreationDTO dto) {

        List list = listService.createList(dto);
        //java.util.List<FilmPreviewResponseDTO> films = filmListService.getFilmsFromList(dto.filmIds());

        ListResponseDTO response = listMapper.mapListToListResponseDTO(list);
        //response.setFilms(films);

        CustomResponseWrapper<ListResponseDTO> wrapper = CustomResponseWrapper.<ListResponseDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message("List has been successfully created")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.CREATED);
    }

    @PutMapping("/add-comment")
    public ResponseEntity<CustomResponseWrapper<ListResponseDTO>> addListComment(
            @RequestParam("listId") Long listId, @RequestParam("commentId") Long commentId) {

        List list = listService.addListComment(listId, commentId);

        ListResponseDTO response = listMapper.mapListToListResponseDTO(list);

        CustomResponseWrapper<ListResponseDTO> wrapper = CustomResponseWrapper.<ListResponseDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message("Comment has been added")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-comment")
    public ResponseEntity<CustomResponseWrapper<ListResponseDTO>> deleteListComment(
            @RequestParam("listId") Long listId, @RequestParam("commentId") Long commentId) {

        List list = listService.deleteListComment(listId, commentId);

        ListResponseDTO response = listMapper.mapListToListResponseDTO(list);

        CustomResponseWrapper<ListResponseDTO> wrapper = CustomResponseWrapper.<ListResponseDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message("Comment has been added deleted")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.CREATED);
    }

    @PatchMapping("/update-likes")
    public ResponseEntity<CustomResponseWrapper<ListResponseDTO>> updateListLikes(
            @RequestParam("listId") Long listId, @RequestParam("userId") Long userId) {

        List list = listService.updateListLikes(listId, userId);

        ListResponseDTO response = listMapper.mapListToListResponseDTO(list);

        CustomResponseWrapper<ListResponseDTO> wrapper = CustomResponseWrapper.<ListResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("List likes has been updated")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PatchMapping("/{listId}/update")
    public ResponseEntity<CustomResponseWrapper<ListResponseDTO>> updateFilmList(
            @RequestBody @Valid ListUpdateDTO dto, @PathVariable Long listId) {

        List list = listService.updateList(dto, listId);
        java.util.List<Long> filmIds = list.getFilmIds().stream().sorted(Comparator.comparing(ListEntry::getEntryOrder)).map(ListEntry::getFilmId).toList();

        //java.util.List<FilmPreviewResponseDTO> films = filmListService.getFilmsFromList(filmIds);

        ListResponseDTO response = listMapper.mapListToListResponseDTO(list);
        //response.setFilms(films);

        CustomResponseWrapper<ListResponseDTO> wrapper = CustomResponseWrapper.<ListResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("List has been successfully updated")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @DeleteMapping("/{listId}/delete")
    public ResponseEntity<CustomResponseWrapper<ListResponseDTO>> deleteFilmList(@PathVariable Long listId) {
        listService.deleteList(listId);
        CustomResponseWrapper<ListResponseDTO> wrapper = CustomResponseWrapper.<ListResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("List has been deleted")
                .data(null)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/by-id/{listId}")
    public ResponseEntity<CustomResponseWrapper<ListResponseDTO>> getListById(@PathVariable Long listId) {

        List list = listService.getListById(listId);
        java.util.List<Long> filmIds = list.getFilmIds().stream().sorted(Comparator.comparing(ListEntry::getEntryOrder)).map(ListEntry::getFilmId).toList();

        java.util.List<FilmPreviewResponseDTO> films = listService.getFilmsFromListById(filmIds);

        ListResponseDTO response = listMapper.mapListToListResponseDTO(list);
        //response.setFilms(films);

        java.util.List<CommentResponseDTO> latestComments = listService.getLatestComments(listId);
        //response.setComments(latestComments);

        CustomResponseWrapper<ListResponseDTO> wrapper = CustomResponseWrapper.<ListResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("List details by ID: %s", listId))
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping({"/{title}", "/{title}/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<ListPageViewResponseDTO>> getListByTitle(
            @PathVariable String title, @PathVariable Optional<Integer> pageNo) {

        List list = listService.getListByTitle(title);
        ListResponseDTO listDTO = listMapper.mapListToListResponseDTO(list);

        java.util.List<FilmPreviewResponseDTO> films;
        if (pageNo.isPresent()) {
            films = listService.getFilmsFromList(list.getFilmIds(), "list-order", pageNo.get());
        } else {
            films = listService.getFilmsFromList(list.getFilmIds(), "list-order", 1);
        }
        PageableFilmPreviewResponseDTO filmsPageable = listMapper.mapFilmPreviewToPageableFilmReviewDTO(pageNo, films.size(), list.getFilmIds().size(), films);

        PageableCommentResponseDTO commentsPageable = listService.getCommentsFromList(list.getId(), 1);

        CustomResponseWrapper<ListPageViewResponseDTO> wrapper = CustomResponseWrapper.<ListPageViewResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("List details by title: %s", title))
                .data(new ListPageViewResponseDTO(listDTO, filmsPageable, commentsPageable))
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping({"/user/{username}/list/{title}", "/user/{username}/list/{title}/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<ListPageViewResponseDTO>> getListByTitleAndUsername(
            @PathVariable String title, @PathVariable String username, @PathVariable Optional<Integer> pageNo) {

        List list = listService.getListByTitleAndUsername(title, username);
        ListResponseDTO listDTO = listMapper.mapListToListResponseDTO(list);

        java.util.List<FilmPreviewResponseDTO> films;
        if (pageNo.isPresent()) {
            films = listService.getFilmsFromList(list.getFilmIds(), "list-order", pageNo.get());
        } else {
            films = listService.getFilmsFromList(list.getFilmIds(), "list-order", 1);
        }
        PageableFilmPreviewResponseDTO filmsPageable = listMapper.mapFilmPreviewToPageableFilmReviewDTO(pageNo, films.size(), list.getFilmIds().size(), films);

        PageableCommentResponseDTO commentsPageable = listService.getCommentsFromList(list.getId(), 1);

        CustomResponseWrapper<ListPageViewResponseDTO> wrapper = CustomResponseWrapper.<ListPageViewResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("List details by title: %s and username: %s", title, username))
                .data(new ListPageViewResponseDTO(listDTO, filmsPageable, commentsPageable))
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping({"/user/{username}/list/{title}/by/{sorting}", "/user/{username}/list/{title}/by/{sorting}/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<ListPageViewResponseDTO>> getListByTitleAndUsernameWithSorting(
            @PathVariable String title, @PathVariable String username,
            @PathVariable String sorting, @PathVariable Optional<Integer> pageNo) {

        List list = listService.getListByTitleAndUsername(title, username);
        ListResponseDTO listDTO = listMapper.mapListToListResponseDTO(list);

        java.util.List<FilmPreviewResponseDTO> films;
        if (pageNo.isPresent()) {
            films = listService.getFilmsFromList(list.getFilmIds(), sorting, pageNo.get());
        } else {
            films = listService.getFilmsFromList(list.getFilmIds(), sorting, 1);
        }
        PageableFilmPreviewResponseDTO filmsPageable = listMapper.mapFilmPreviewToPageableFilmReviewDTO(pageNo, films.size(), list.getFilmIds().size(), films);

        PageableCommentResponseDTO commentsPageable = listService.getCommentsFromList(list.getId(), 1);

        CustomResponseWrapper<ListPageViewResponseDTO> wrapper = CustomResponseWrapper.<ListPageViewResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("List details by title: %s and username: %s", title, username))
                .data(new ListPageViewResponseDTO(listDTO, filmsPageable, commentsPageable))
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping({"/user/{username}", "/user/{username}/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<PageableListResponseDTO>> getAllByUser(
            @PathVariable String username, @PathVariable Optional<Integer> pageNo) {

        Page<List> filmLists;
        if (pageNo.isPresent()) {
            filmLists = listService.getListsByUsername(username, "when-updated", pageNo.get());
        } else {
            filmLists = listService.getListsByUsername(username, "when-updated", 1);
        }

        PageableListResponseDTO response = listMapper.mapListsPageToPageableListResponseDTO(filmLists);

        CustomResponseWrapper<PageableListResponseDTO> wrapper = CustomResponseWrapper.<PageableListResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Film lists of user: %s", username))
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping({"/user/{username}/by/{sorting}", "/user/{username}/by/{sorting}/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<PageableListResponseDTO>> getAllByUserWithSorting(
            @PathVariable String username, @PathVariable String sorting, @PathVariable Optional<Integer> pageNo) {

        Page<List> filmLists;
        if (pageNo.isPresent()) {
            filmLists = listService.getListsByUsername(username, sorting, pageNo.get());
        } else {
            filmLists = listService.getListsByUsername(username, sorting, 1);
        }

        PageableListResponseDTO response = listMapper.mapListsPageToPageableListResponseDTO(filmLists);

        CustomResponseWrapper<PageableListResponseDTO> wrapper = CustomResponseWrapper.<PageableListResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Film lists of user: %s with sorting: %s", username, sorting))
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping({"", "/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<PageableListResponseDTO>> getAll(@PathVariable Optional<Integer> pageNo) {

        Page<List> allLists;
        if (pageNo.isPresent()) {
            allLists = listService.getAllLists(pageNo.get());
        } else {
            allLists = listService.getAllLists(1);
        }

        PageableListResponseDTO response = listMapper.mapListsPageToPageableListResponseDTO(allLists);

        CustomResponseWrapper<PageableListResponseDTO> wrapper = CustomResponseWrapper.<PageableListResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("All film list")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping({"/by/{sorting}", "/by/{sorting}/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<PageableListResponseDTO>> getAllWithSorting(
            @PathVariable String sorting, @PathVariable Optional<Integer> pageNo) {

        Page<List> allLists;
        if (pageNo.isPresent()) {
            allLists = listService.getAllListsWithSorting(sorting, pageNo.get());
        } else {
            allLists = listService.getAllListsWithSorting(sorting, 1);
        }

        PageableListResponseDTO response = listMapper.mapListsPageToPageableListResponseDTO(allLists);

        CustomResponseWrapper<PageableListResponseDTO> wrapper = CustomResponseWrapper.<PageableListResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("Film lists sorted by: %s", sorting))
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }
}
