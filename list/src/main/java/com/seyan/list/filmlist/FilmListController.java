package com.seyan.list.filmlist;

import com.seyan.list.comment.CommentResponseDTO;
import com.seyan.list.dto.*;
import com.seyan.list.entry.ListEntry;
import com.seyan.list.handler.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/lists")
@RestController
public class FilmListController {
    private final FilmListService filmListService;
    private final FilmListMapper filmListMapper;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createFilmList(@RequestBody @Valid FilmListCreationDTO dto) {

        FilmList list = filmListService.createList(dto);
        List<FilmInFilmListResponseDTO> films = filmListService.getFilmsFromList(dto.filmIds());

        FilmListResponseDTO response = filmListMapper.mapFilmListToFilmListResponseDTO(list);
        response.setFilms(films);

        return ResponseHandler.generateResponse("List has been successfully created", HttpStatus.CREATED, response);
    }

    @PutMapping("/add-comment")
    public ResponseEntity<Object> addListComment(
            @RequestParam("listId") Long listId, @RequestParam("commentId") Long commentId) {

        FilmList list = filmListService.addListComment(listId, commentId);
        FilmListResponseDTO response = filmListMapper.mapFilmListToFilmListResponseDTO(list);

        return ResponseHandler.generateResponse("Comment has been added", HttpStatus.CREATED, response);
    }

    @DeleteMapping("/delete-comment")
    public ResponseEntity<Object> deleteListComment(
            @RequestParam("listId") Long listId, @RequestParam("commentId") Long commentId) {

        FilmList list = filmListService.deleteListComment(listId, commentId);
        FilmListResponseDTO response = filmListMapper.mapFilmListToFilmListResponseDTO(list);

        return ResponseHandler.generateResponse("Comment has been added deleted", HttpStatus.OK, response);
    }

    @PatchMapping("/update-likes")
    public ResponseEntity<Object> updateListLikes(
            @RequestParam("listId") Long listId, @RequestParam("userId") Long userId) {

        FilmList list = filmListService.updateListLikes(listId, userId);

        FilmListResponseDTO response = filmListMapper.mapFilmListToFilmListResponseDTO(list);



        return ResponseHandler.generateResponse("List likes has been updated", HttpStatus.OK, response);
    }

    @PatchMapping("/{listId}/update")
    public ResponseEntity<Object> updateFilmList(
            @RequestBody @Valid FilmListUpdateDTO dto, @PathVariable("listId") Long listId) {

        FilmList list = filmListService.updateFilmList(dto, listId);
        List<Long> filmIds = list.getFilmIds().stream()
                .sorted(Comparator.comparing(ListEntry::getEntryOrder))
                .map(ListEntry::getFilmId).toList();

        List<FilmInFilmListResponseDTO> films = filmListService.getFilmsFromList(filmIds);

        FilmListResponseDTO response = filmListMapper.mapFilmListToFilmListResponseDTO(list);
        response.setFilms(films);

        return ResponseHandler.generateResponse("List has been successfully updated", HttpStatus.OK, response);
    }

    @DeleteMapping("/{listId}/delete")
    public ResponseEntity<Object> deleteFilmList(@PathVariable("listId") Long listId) {
        filmListService.deleteFilmList(listId);

        return ResponseHandler.generateResponse("List has been deleted", HttpStatus.OK, null);
    }

    @GetMapping("/{listId}")
    public ResponseEntity<Object> getListById(@PathVariable("listId") Long listId) {

        FilmList list = filmListService.getFilmListById(listId);
        List<Long> filmIds = list.getFilmIds().stream()
                .sorted(Comparator.comparing(ListEntry::getEntryOrder))
                .map(ListEntry::getFilmId).toList();

        List<FilmInFilmListResponseDTO> films = filmListService.getFilmsFromList(filmIds);

        FilmListResponseDTO response = filmListMapper.mapFilmListToFilmListResponseDTO(list);
        response.setFilms(films);

        List<CommentResponseDTO> latestComments = filmListService.getLatestComments(listId);
        response.setComments(latestComments);

        return ResponseHandler.generateResponse(String.format("List details by ID: %s", listId), HttpStatus.OK, response);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        List<FilmList> allLists = filmListService.getAllFilmLists();
        List<FilmListResponseDTO> response = filmListMapper.mapFilmListToFilmListResponseDTO(allLists);


        return ResponseHandler.generateResponse("All film lists", HttpStatus.OK, response);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<Object> getAllByUserId(@PathVariable("userId") Long userId) {
        List<FilmList> allLists = filmListService.getAllFilmListsByUserId(userId);

        List<FilmListResponseDTO> response = filmListMapper.mapFilmListToFilmListResponseDTO(allLists);


        return ResponseHandler.generateResponse(String.format("Film lists by user ID: %s", userId), HttpStatus.OK, response);
    }
}
