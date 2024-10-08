package com.seyan.film.dto.film;


import com.seyan.film.dto.profile.ProfilePreviewResponseDTO;
import com.seyan.film.film.Film;
import com.seyan.film.profile.Profile;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FilmMapper {
    public PageableFilmPreviewResponseDTO mapFilmsPageToPageableFilmPreviewResponseDTO(Page<Film> films) {
        List<FilmPreviewResponseDTO> mapped = mapFilmToFilmPreviewResponseDTO(films.getContent());

        return PageableFilmPreviewResponseDTO.builder()
                .content(mapped)
                .pageNo(films.getNumber())
                .pageSize(films.getSize())
                .totalPages(films.getTotalPages())
                .last(films.isLast())
                .build();
    }

    public PageableFilmResponseDTO mapFilmsPageToPageableFilmResponseDTO(Page<Film> comments) {
        List<FilmResponseDTO> mapped = mapFilmToFilmResponseDTO(comments.getContent());

        return PageableFilmResponseDTO.builder()
                .content(mapped)
                .pageNo(comments.getNumber())
                .pageSize(comments.getSize())
                .totalPages(comments.getTotalPages())
                .last(comments.isLast())
                .build();
    }

    public Film mapFilmCreationDTOToFilm(FilmCreationDTO dto) {
        Film film = new Film();
        BeanUtils.copyProperties(dto, film, getNullFieldNames(dto));
        return film;
    }

    public FilmPreviewResponseDTO mapFilmToFilmPreviewResponseDTO(Film film) {
        if (film == null) {
            return null;
        }
        return new FilmPreviewResponseDTO(film.getId(), film.getTitle(), film.getUrl());
    }

    public List<FilmPreviewResponseDTO> mapFilmToFilmPreviewResponseDTO(List<Film> films) {
        if (films == null) {
            return null;
        }
        return films.stream()
                .map(this::mapFilmToFilmPreviewResponseDTO)
                .toList();
    }

    public Film mapFilmUpdateDTOToFilm(FilmUpdateDTO source, Film destination) {
        BeanUtils.copyProperties(source, destination, getNullFieldNames(source));
        return destination;
    }

    private List<ProfilePreviewResponseDTO> mapProfileToProfilePreviewResponseDTO(Set<Profile> profiles) {
        if (profiles == null) {
            return null;
        }
        return profiles.stream()
                .map(this::mapProfileToProfilePreviewResponseDTO)
                .toList();
    }

    private ProfilePreviewResponseDTO mapProfileToProfilePreviewResponseDTO(Profile profile) {
        if (profile == null) {
            return null;
        }
        return new ProfilePreviewResponseDTO(profile.getId(), profile.getName(), profile.getUrl());
    }

    public FilmResponseDTO mapFilmToFilmResponseDTO(Film film) {
        List<ProfilePreviewResponseDTO> cast = mapProfileToProfilePreviewResponseDTO(film.getCast());
        ProfilePreviewResponseDTO director = mapProfileToProfilePreviewResponseDTO(film.getDirector());
        return new FilmResponseDTO(
                film.getId(),
                film.getTitle(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getUrl(),
                film.getAvgRating(),
                director,
                cast,
                film.getGenre(),
                film.getRunningTimeMinutes(),
                film.getWatchedCount(),
                film.getListCount(),
                film.getLikeCount()
        );
    }

    public List<FilmResponseDTO> mapFilmToFilmResponseDTO(List<Film> films) {
        if (films == null) {
            return null;
        }

        return films.stream()
                .map(this::mapFilmToFilmResponseDTO)
                .toList();
    }

    private String[] getNullFieldNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> fieldNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                fieldNames.add(pd.getName());
        }

        String[] result = new String[fieldNames.size()];
        return fieldNames.toArray(result);
    }
}
