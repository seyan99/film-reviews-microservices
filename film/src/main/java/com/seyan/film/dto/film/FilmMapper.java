package com.seyan.film.dto.film;


import com.seyan.film.film.Film;
import com.seyan.film.profile.Profile;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FilmMapper {
    public Film mapFilmCreationDTOToFilm(FilmCreationDTO dto) {
        Film film = new Film();
        BeanUtils.copyProperties(dto, film, getNullFieldNames(dto));
        return film;
    }

    public FilmInFilmListResponseDTO mapFilmToFilmInFilmListResponseDTO(Film film) {
        if (film == null) {
            return null;
        }
        return new FilmInFilmListResponseDTO(film.getId(), film.getTitle(), film.getUrl());
    }

    public List<FilmInFilmListResponseDTO> mapFilmToFilmInFilmListResponseDTO(List<Film> films) {
        if (films == null) {
            return null;
        }
        return films.stream()
                .map(this::mapFilmToFilmInFilmListResponseDTO)
                .toList();
    }

    public Film mapFilmUpdateDTOToFilm(FilmUpdateDTO source, Film destination) {
        BeanUtils.copyProperties(source, destination, getNullFieldNames(source));
        return destination;
    }

    private List<ProfileInFilmResponseDTO> mapProfileToProfileInFilmResponseDTO(Set<Profile> profiles) {
        if (profiles == null) {
            return null;
        }
        return profiles.stream()
                .map(this::mapProfileToProfileInFilmResponseDTO)
                .toList();
    }

    private ProfileInFilmResponseDTO mapProfileToProfileInFilmResponseDTO(Profile profile) {
        if (profile == null) {
            return null;
        }
        return new ProfileInFilmResponseDTO(profile.getId(), profile.getName(), profile.getUrl());
    }

    public FilmResponseDTO mapFilmToFilmResponseDTO(Film film) {
        List<ProfileInFilmResponseDTO> cast = mapProfileToProfileInFilmResponseDTO(film.getCast());
        ProfileInFilmResponseDTO director = mapProfileToProfileInFilmResponseDTO(film.getDirector());
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
