package com.seyan.film.film;

import com.seyan.film.external.activity.ActivityOnFilmResponseDTO;
import com.seyan.film.dto.film.*;
import com.seyan.film.responsewrapper.CustomResponseWrapper;
import com.seyan.film.external.review.ReviewResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/api/v1/films")
@RestController
public class FilmController {
    private final FilmService filmService;
    private final FilmMapper filmMapper;

    @PostMapping("/create")
    public ResponseEntity<CustomResponseWrapper<FilmResponseDTO>> createFilm(@RequestBody @Valid FilmCreationDTO dto) {
        Film film = filmService.createFilm(dto);
        FilmResponseDTO response = filmMapper.mapFilmToFilmResponseDTO(film);
        CustomResponseWrapper<FilmResponseDTO> wrapper = CustomResponseWrapper.<FilmResponseDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message("Film has been successfully created")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<CustomResponseWrapper<FilmResponseDTO>> updateFilm(@PathVariable("id") Long id, @RequestBody @Valid FilmUpdateDTO dto) {
        Film film = filmService.updateFilm(dto, id);
        FilmResponseDTO response = filmMapper.mapFilmToFilmResponseDTO(film);
        CustomResponseWrapper<FilmResponseDTO> wrapper = CustomResponseWrapper.<FilmResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Film has been updated")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PatchMapping("/update-like-count")
    public ResponseEntity<CustomResponseWrapper<FilmResponseDTO>> updateLikeCount(@RequestParam("id") Long id, @RequestParam Boolean toAdd) {
        Film film = filmService.updateLikeCount(id, toAdd);
        FilmResponseDTO response = filmMapper.mapFilmToFilmResponseDTO(film);
        CustomResponseWrapper<FilmResponseDTO> wrapper = CustomResponseWrapper.<FilmResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Film like count has been updated")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PatchMapping("/update-watched-count")
    public ResponseEntity<CustomResponseWrapper<FilmResponseDTO>> updateWatchedCount(@RequestParam("id") Long id, @RequestParam Boolean toAdd) {
        Film film = filmService.updateWatchedCount(id, toAdd);
        FilmResponseDTO response = filmMapper.mapFilmToFilmResponseDTO(film);
        CustomResponseWrapper<FilmResponseDTO> wrapper = CustomResponseWrapper.<FilmResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Film has been updated")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    //todo as map?
    @PatchMapping("/update-avg-rating")
    public ResponseEntity<CustomResponseWrapper<FilmResponseDTO>> updateAvgRating(@RequestParam("id") Long id, @RequestParam Double rating) {
        Film film = filmService.updateAvgRating(id, rating);
        FilmResponseDTO response = filmMapper.mapFilmToFilmResponseDTO(film);
        CustomResponseWrapper<FilmResponseDTO> wrapper = CustomResponseWrapper.<FilmResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Film rating has been updated")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<CustomResponseWrapper<FilmResponseDTO>> deleteFilm(@PathVariable("id") Long filmId) {
        filmService.deleteFilm(filmId);
        CustomResponseWrapper<FilmResponseDTO> wrapper = CustomResponseWrapper.<FilmResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Film has been deleted")
                .data(null)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<CustomResponseWrapper<FilmResponseDTO>> filmDetailsById(@PathVariable(value = "id") Long id) {
        Film film = filmService.getFilmById(id);
        FilmResponseDTO response = filmMapper.mapFilmToFilmResponseDTO(film);
        CustomResponseWrapper<FilmResponseDTO> wrapper = CustomResponseWrapper.<FilmResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Film details")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping("/get-by-id-list")
    public ResponseEntity<CustomResponseWrapper<List<FilmPreviewResponseDTO>>> getFilmsByIdList(@RequestBody List<Long> filmIds) {
        List<Film> films = filmService.getFilmsByIdList(filmIds);
        List<FilmPreviewResponseDTO> response = filmMapper.mapFilmToFilmPreviewResponseDTO(films);
        CustomResponseWrapper<List<FilmPreviewResponseDTO>> wrapper = CustomResponseWrapper.<List<FilmPreviewResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Films by id list")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    /*@GetMapping("/{filmUrl}")
    public ResponseEntity<CustomResponseWrapper<FilmResponseDTO>> filmDetailsByUrl(@PathVariable(value = "filmUrl") String filmUrl) {
        Film film = filmService.getFilmByUrl(filmUrl);
        FilmResponseDTO response = filmMapper.mapFilmToFilmResponseDTO(film);
        CustomResponseWrapper<FilmResponseDTO> wrapper = CustomResponseWrapper.<FilmResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Film details")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }*/

    //todo get user id from principal
    @GetMapping("/{filmUrl}")
    public ResponseEntity<CustomResponseWrapper<FilmPageViewResponseDTO>> filmDetailsByUrl(
            @PathVariable(value = "filmUrl") String filmUrl, @RequestParam Long userId) {

        Film film = filmService.getFilmByUrl(filmUrl);
        FilmResponseDTO filmDTO = filmMapper.mapFilmToFilmResponseDTO(film);
        ActivityOnFilmResponseDTO activity = filmService.getFilmActivity(userId, film.getId());
        Map<String, List<ReviewResponseDTO>> reviews = filmService.getLatestAndPopularReviewsForFilm(film.getId());
        CustomResponseWrapper<FilmPageViewResponseDTO> wrapper = CustomResponseWrapper.<FilmPageViewResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Film details")
                .data(new FilmPageViewResponseDTO(filmDTO, activity, reviews))
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    //todo delete ?
    @GetMapping
    public ResponseEntity<CustomResponseWrapper<List<FilmResponseDTO>>> getAllFilms(
            @RequestParam(required = false) Map<String, String> params, @RequestParam(required = false) Long userId) {
        List<Film> films = filmService.getAllFilmsWithParams(params, userId);
        List<FilmResponseDTO> response = filmMapper.mapFilmToFilmResponseDTO(films);
        CustomResponseWrapper<List<FilmResponseDTO>> wrapper = CustomResponseWrapper.<List<FilmResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("List of all films")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    //todo replace userId with principal
    @GetMapping({"/decade/{decade}/genre/{genre}/by/{sorting}", "/decade/{decade}/genre/{genre}/by/{sorting}/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<PageableFilmResponseDTO>> getFilmsWithDecadeAndGenreAndSorting(
            @PathVariable String decade, @PathVariable String genre,
            @PathVariable String sorting, @PathVariable Optional<Integer> pageNo,
            @RequestParam(required = false) Long userId) {

        Page<Film> films;
        if (pageNo.isPresent()) {
            films = filmService.getFilmsWithDecadeAndGenreAndSorting(decade, genre, sorting, pageNo.get());
        } else {
            films = filmService.getFilmsWithDecadeAndGenreAndSorting(decade, genre, sorting, 1);
        }

        PageableFilmResponseDTO response = filmMapper.mapFilmsPageToPageableFilmResponseDTO(films);
        CustomResponseWrapper<PageableFilmResponseDTO> wrapper = CustomResponseWrapper.<PageableFilmResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Films by decade, genre and sorting")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping({"/decade/{decade}/by/{sorting}", "/decade/{decade}/by/{sorting}/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<PageableFilmResponseDTO>> getFilmsWithDecadeAndSorting(
            @PathVariable String decade, @PathVariable String sorting,
            @PathVariable Optional<Integer> pageNo, @RequestParam(required = false) Long userId) {

        Page<Film> films;
        if (pageNo.isPresent()) {
            films = filmService.getFilmsWithDecadeAndGenreAndSorting(decade, null, sorting, pageNo.get());
        } else {
            films = filmService.getFilmsWithDecadeAndGenreAndSorting(decade, null, sorting, 1);
        }

        PageableFilmResponseDTO response = filmMapper.mapFilmsPageToPageableFilmResponseDTO(films);
        CustomResponseWrapper<PageableFilmResponseDTO> wrapper = CustomResponseWrapper.<PageableFilmResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Films by genre and sorting")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping({"/genre/{genre}/by/{sorting}", "/genre/{genre}/by/{sorting}/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<PageableFilmResponseDTO>> getFilmsWithGenreAndSorting(
            @PathVariable String genre, @PathVariable String sorting,
            @PathVariable Optional<Integer> pageNo, @RequestParam(required = false) Long userId) {

        Page<Film> films;
        if (pageNo.isPresent()) {
            films = filmService.getFilmsWithDecadeAndGenreAndSorting(null, genre, sorting, pageNo.get());
        } else {
            films = filmService.getFilmsWithDecadeAndGenreAndSorting(null, genre, sorting, 1);
        }

        PageableFilmResponseDTO response = filmMapper.mapFilmsPageToPageableFilmResponseDTO(films);
        CustomResponseWrapper<PageableFilmResponseDTO> wrapper = CustomResponseWrapper.<PageableFilmResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Films by genre and sorting")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    //todo overload service method cuz null is not good
    @GetMapping({"/by/{sorting}", "/by/{sorting}/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<PageableFilmResponseDTO>> getFilmsWithSorting(
            @PathVariable String sorting, @PathVariable Optional<Integer> pageNo,
            @RequestParam(required = false) Long userId) {

        Page<Film> films;
        if (pageNo.isPresent()) {
            films = filmService.getFilmsWithDecadeAndGenreAndSorting(null, null, sorting, pageNo.get());
        } else {
            films = filmService.getFilmsWithDecadeAndGenreAndSorting(null, null, sorting, 1);
        }

        PageableFilmResponseDTO response = filmMapper.mapFilmsPageToPageableFilmResponseDTO(films);
        CustomResponseWrapper<PageableFilmResponseDTO> wrapper = CustomResponseWrapper.<PageableFilmResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Films by sorting")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping({"/search/{title}", "/search/{title}/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<PageableFilmResponseDTO>> getFilmsByTitlePaginated(
            @PathVariable String title, @PathVariable("pageNo") Optional<Integer> pageNo) {

        Page<Film> films;
        if (pageNo.isPresent()) {
            films = filmService.getAllFilmsByTitle(title, pageNo.get());
        } else {
            films = filmService.getAllFilmsByTitle(title, 1);
        }
        PageableFilmResponseDTO response = filmMapper.mapFilmsPageToPageableFilmResponseDTO(films);
        CustomResponseWrapper<PageableFilmResponseDTO> wrapper = CustomResponseWrapper.<PageableFilmResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("List of films by title")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @GetMapping({"/from-list", "/from-list/page/{pageNo}"})
    public ResponseEntity<CustomResponseWrapper<PageableFilmPreviewResponseDTO>> getFilmsFromList(
            @PathVariable("pageNo") Optional<Integer> pageNo, @RequestBody List<Long> filmIds) {

        Page<Film> films;
        if (pageNo.isPresent()) {
            films = filmService.getFilmsFromList(filmIds, pageNo.get());
        } else {
            films = filmService.getFilmsFromList(filmIds, 1);
        }
        PageableFilmPreviewResponseDTO response = filmMapper.mapFilmsPageToPageableFilmPreviewResponseDTO(films);
        CustomResponseWrapper<PageableFilmPreviewResponseDTO> wrapper = CustomResponseWrapper.<PageableFilmPreviewResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Films preview")
                .data(response)
                .build();
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PatchMapping("{id}/update/add-cast")
    public ResponseEntity<CustomResponseWrapper<FilmResponseDTO>> addFilmCastMember(
            @PathVariable(value = "id") Long filmId, @RequestBody List<Long> profileIdList) {

        Film film = filmService.addCastMember(profileIdList, filmId);
        FilmResponseDTO response = filmMapper.mapFilmToFilmResponseDTO(film);

        CustomResponseWrapper<FilmResponseDTO> wrapper = CustomResponseWrapper.<FilmResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Film with updated cast")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PatchMapping("{id}/update/remove-cast")
    public ResponseEntity<CustomResponseWrapper<FilmResponseDTO>> removeFilmCastMember
            (@PathVariable(value = "id") Long filmId, @RequestBody List<Long> profileIdList) {

        Film film = filmService.removeCastMember(profileIdList, filmId);
        FilmResponseDTO response = filmMapper.mapFilmToFilmResponseDTO(film);

        CustomResponseWrapper<FilmResponseDTO> wrapper = CustomResponseWrapper.<FilmResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Film with updated cast")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PatchMapping("{id}/update/director")
    public ResponseEntity<CustomResponseWrapper<FilmResponseDTO>> updateFilmDirector
            (@PathVariable(value = "id") Long filmId, @RequestParam(required = false) Long directorId) {

        Film film = filmService.updateDirector(directorId, filmId);
        FilmResponseDTO response = filmMapper.mapFilmToFilmResponseDTO(film);

        CustomResponseWrapper<FilmResponseDTO> wrapper = CustomResponseWrapper.<FilmResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Film with updated cast")
                .data(response)
                .build();

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }
}
