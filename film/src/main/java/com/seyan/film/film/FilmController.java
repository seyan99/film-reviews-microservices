package com.seyan.film.film;

import com.seyan.film.dto.film.*;
import com.seyan.film.handler.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/v1/films")
@RestController
public class FilmController {
    private final FilmService filmService;
    private final FilmMapper filmMapper;

    @PostMapping("/create")
    public ResponseEntity<Object> createFilm(@RequestBody @Valid FilmCreationDTO dto) {
        Film film = filmService.createFilm(dto);
        FilmResponseDTO response = filmMapper.mapFilmToFilmResponseDTO(film);

        return ResponseHandler.generateResponse("Film has been successfully created", HttpStatus.CREATED, response);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<Object> updateFilm(@PathVariable("id") Long id, @RequestBody @Valid FilmUpdateDTO dto) {
        Film film = filmService.updateFilm(dto, id);
        FilmResponseDTO response = filmMapper.mapFilmToFilmResponseDTO(film);

        return ResponseHandler.generateResponse("Film has been updated", HttpStatus.OK, response);
    }

    @PatchMapping("/update-like-count")
    public ResponseEntity<Object> updateLikeCount(@RequestParam("id") Long id, @RequestParam Boolean toAdd) {
        Film film = filmService.updateLikeCount(id, toAdd);
        FilmResponseDTO response = filmMapper.mapFilmToFilmResponseDTO(film);

        return ResponseHandler.generateResponse("Film like count has been updated", HttpStatus.OK, response);
    }

    @PatchMapping("/update-watched-count")
    public ResponseEntity<Object> updateWatchedCount(@RequestParam("id") Long id, @RequestParam Boolean toAdd) {
        Film film = filmService.updateWatchedCount(id, toAdd);
        FilmResponseDTO response = filmMapper.mapFilmToFilmResponseDTO(film);

        return ResponseHandler.generateResponse("Film watched count has been updated", HttpStatus.OK, response);
    }

    //todo as map?
    @PatchMapping("/update-avg-rating")
    public ResponseEntity<Object> updateAvgRating(@RequestParam("id") Long id, @RequestParam Double rating) {
        Film film = filmService.updateAvgRating(id, rating);
        FilmResponseDTO response = filmMapper.mapFilmToFilmResponseDTO(film);

        return ResponseHandler.generateResponse("Film rating has been updated", HttpStatus.OK, response);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Object> deleteFilm(@PathVariable("id") Long filmId) {
        filmService.deleteFilm(filmId);
        return ResponseHandler.generateResponse("Film has been deleted", HttpStatus.OK, null);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<Object> filmDetailsById(@PathVariable(value = "id") Long id) {
        Film film = filmService.getFilmById(id);
        FilmResponseDTO response = filmMapper.mapFilmToFilmResponseDTO(film);

        return ResponseHandler.generateResponse("Film details", HttpStatus.OK, response);
    }

    @GetMapping("/get-by-id-list")
    public ResponseEntity<Object> getFilmsByIdList(@RequestBody List<Long> filmIds) {
        List<Film> films = filmService.getFilmsByIdList(filmIds);
        List<FilmInFilmListResponseDTO> response = filmMapper.mapFilmToFilmInFilmListResponseDTO(films);

        return ResponseHandler.generateResponse("Films by id list", HttpStatus.OK, response);
    }

    @GetMapping("/{filmUrl}")
    public ResponseEntity<Object> filmDetailsByUrl(@PathVariable(value = "filmUrl") String filmUrl) {
        Film film = filmService.getFilmByUrl(filmUrl);
        FilmResponseDTO response = filmMapper.mapFilmToFilmResponseDTO(film);

        return ResponseHandler.generateResponse("Film details", HttpStatus.OK, response);
    }

    @GetMapping
    public ResponseEntity<Object> getAllFilms(
            @RequestParam(required = false) Map<String, String> params, @RequestParam(required = false) Long userId) {

        List<Film> films = filmService.getAllFilmsWithParams(params, userId);
        List<FilmResponseDTO> response = filmMapper.mapFilmToFilmResponseDTO(films);

        return ResponseHandler.generateResponse("List of all films", HttpStatus.OK, response);
    }

    @GetMapping("/search/{title}")
    public ResponseEntity<Object> getAllFilmsByTitle(@PathVariable String title) {
        //todo parse title with blank spaces(?)

        List<Film> films = filmService.getAllFilmsByTitle(title);
        List<FilmResponseDTO> response = filmMapper.mapFilmToFilmResponseDTO(films);

        return ResponseHandler.generateResponse("List of all films by title", HttpStatus.OK, response);
    }

    @PatchMapping("{id}/update/add-cast")
    public ResponseEntity<Object> addFilmCastMember(
            @PathVariable(value = "id") Long filmId, @RequestBody List<Long> profileIdList) {

        Film film = filmService.addCastMember(profileIdList, filmId);
        FilmResponseDTO response = filmMapper.mapFilmToFilmResponseDTO(film);

        return ResponseHandler.generateResponse("Film with updated cast", HttpStatus.OK, response);
    }

    @PatchMapping("{id}/update/remove-cast")
    public ResponseEntity<Object> removeFilmCastMember
            (@PathVariable(value = "id") Long filmId, @RequestBody List<Long> profileIdList) {

        Film film = filmService.removeCastMember(profileIdList, filmId);
        FilmResponseDTO response = filmMapper.mapFilmToFilmResponseDTO(film);

        return ResponseHandler.generateResponse("Film with updated cast", HttpStatus.OK, response);
    }

    @PatchMapping("{id}/update/director")
    public ResponseEntity<Object> updateFilmDirector
            (@PathVariable(value = "id") Long filmId, @RequestParam(required = false) Long directorId) {

        Film film = filmService.updateDirector(directorId, filmId);
        FilmResponseDTO response = filmMapper.mapFilmToFilmResponseDTO(film);

        return ResponseHandler.generateResponse("Film with updated director", HttpStatus.OK, response);
    }
}
