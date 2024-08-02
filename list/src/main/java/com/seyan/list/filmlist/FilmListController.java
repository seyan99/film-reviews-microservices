package com.seyan.list.filmlist;

import com.seyan.list.dto.*;
import com.seyan.list.responsewrapper.CustomResponseWrapper;
import com.seyan.list.entry.ListEntry;
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
    public ResponseEntity<CustomResponseWrapper<FilmListResponseDTO>> createFilmList(@RequestBody @Valid FilmListCreationDTO dto) {

        FilmList list = filmListService.createList(dto);
        List<FilmInFilmListResponseDTO> films = filmListService.getFilmsFromList(dto.filmIds());

        FilmListResponseDTO response = filmListMapper.mapFilmListToFilmListResponseDTO(list);
        response.setFilms(films);

        CustomResponseWrapper<FilmListResponseDTO> wrapper = CustomResponseWrapper.<FilmListResponseDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message("List has been successfully created")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.CREATED);
    }

    @PutMapping("/add-comment")
    public ResponseEntity<CustomResponseWrapper<FilmListResponseDTO>> addListComment(@RequestParam("listId") Long listId, @RequestParam("commentId") Long commentId) {

        FilmList list = filmListService.addListComment(listId, commentId);
        //List<FilmInFilmListResponseDTO> films = filmListService.getFilmsFromList(dto.filmIds());

        FilmListResponseDTO response = filmListMapper.mapFilmListToFilmListResponseDTO(list);
        //response.setFilms(films);

        CustomResponseWrapper<FilmListResponseDTO> wrapper = CustomResponseWrapper.<FilmListResponseDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message("List has been successfully created")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-comment")
    public ResponseEntity<CustomResponseWrapper<FilmListResponseDTO>> deleteListComment(@RequestParam("listId") Long listId, @RequestParam("commentId") Long commentId) {

        FilmList list = filmListService.deleteListComment(listId, commentId);
        //List<FilmInFilmListResponseDTO> films = filmListService.getFilmsFromList(dto.filmIds());

        FilmListResponseDTO response = filmListMapper.mapFilmListToFilmListResponseDTO(list);
        //response.setFilms(films);

        CustomResponseWrapper<FilmListResponseDTO> wrapper = CustomResponseWrapper.<FilmListResponseDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message("List has been successfully created")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.CREATED);
    }

    @PatchMapping("/update-likes")
    public ResponseEntity<CustomResponseWrapper<FilmListResponseDTO>> updateListLikes(@RequestParam("listId") Long listId, @RequestParam("userId") Long userId) {

        FilmList list = filmListService.updateListLikes(listId, userId);
        //List<Long> filmIds = list.getFilmIds().stream().sorted(Comparator.comparing(ListEntry::getEntryOrder)).map(ListEntry::getFilmId).toList();

        //List<FilmInFilmListResponseDTO> films = filmListService.getFilmsFromList(filmIds);

        FilmListResponseDTO response = filmListMapper.mapFilmListToFilmListResponseDTO(list);
        //response.setFilms(films);

        CustomResponseWrapper<FilmListResponseDTO> wrapper = CustomResponseWrapper.<FilmListResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("List likes has been updated")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    //todo /username/list/listname/edit/

    @PatchMapping("/{listId}/update")
    public ResponseEntity<CustomResponseWrapper<FilmListResponseDTO>> updateFilmList(@RequestBody @Valid FilmListUpdateDTO dto, @PathVariable("listId") Long listId) {

        FilmList list = filmListService.updateFilmList(dto, listId);
        List<Long> filmIds = list.getFilmIds().stream().sorted(Comparator.comparing(ListEntry::getEntryOrder)).map(ListEntry::getFilmId).toList();

        List<FilmInFilmListResponseDTO> films = filmListService.getFilmsFromList(filmIds);

        FilmListResponseDTO response = filmListMapper.mapFilmListToFilmListResponseDTO(list);
        response.setFilms(films);

        CustomResponseWrapper<FilmListResponseDTO> wrapper = CustomResponseWrapper.<FilmListResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("List has been successfully updated")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @DeleteMapping("/{listId}/delete")
    public ResponseEntity<CustomResponseWrapper<FilmListResponseDTO>> deleteFilmList(@PathVariable("listId") Long listId) {
        filmListService.deleteFilmList(listId);
        CustomResponseWrapper<FilmListResponseDTO> wrapper = CustomResponseWrapper.<FilmListResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("List has been deleted")
                .data(null)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/{listId}")
    public ResponseEntity<CustomResponseWrapper<FilmListResponseDTO>> getListById(@PathVariable("listId") Long listId) {

        FilmList list = filmListService.getFilmListById(listId);
        List<Long> filmIds = list.getFilmIds().stream().sorted(Comparator.comparing(ListEntry::getEntryOrder)).map(ListEntry::getFilmId).toList();

        List<FilmInFilmListResponseDTO> films = filmListService.getFilmsFromList(filmIds);

        FilmListResponseDTO response = filmListMapper.mapFilmListToFilmListResponseDTO(list);
        response.setFilms(films);

        CustomResponseWrapper<FilmListResponseDTO> wrapper = CustomResponseWrapper.<FilmListResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message(String.format("List details by ID: %s", listId))
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<CustomResponseWrapper<List<FilmListResponseDTO>>> getAll() {
        List<FilmList> allLists = filmListService.getAllFilmLists();

        List<FilmListResponseDTO> response = filmListMapper.mapFilmListToFilmListResponseDTO(allLists);

        CustomResponseWrapper<List<FilmListResponseDTO>> wrapper = CustomResponseWrapper.<List<FilmListResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("All film list")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<CustomResponseWrapper<List<FilmListResponseDTO>>> getAllByUserId(@PathVariable("userId") Long userId) {
        List<FilmList> allLists = filmListService.getAllFilmListsByUserId(userId);

        List<FilmListResponseDTO> response = filmListMapper.mapFilmListToFilmListResponseDTO(allLists);

        CustomResponseWrapper<List<FilmListResponseDTO>> wrapper = CustomResponseWrapper.<List<FilmListResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("All film list")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }
}
