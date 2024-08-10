package com.seyan.film.dto.profile;


import com.seyan.film.dto.film.FilmPreviewResponseDTO;
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
public class ProfileMapper {
    public PageableProfileResponseDTO mapProfilesPageToPageableProfileResponseDTO(Page<Profile> comments) {
        List<ProfileResponseDTO> mapped = mapProfileToProfileResponseDTO(comments.getContent());

        return PageableProfileResponseDTO.builder()
                .content(mapped)
                .pageNo(comments.getNumber())
                .pageSize(comments.getSize())
                .totalPages(comments.getTotalPages())
                .last(comments.isLast())
                .build();
    }

    public Profile mapPofileCreationDTOToProfile(ProfileCreationDTO dto) {
        return Profile.builder()
                .name(dto.name())
                .biography(dto.biography())
                .build();
    }

    public Profile mapProfileUpdateDTOToProfile(ProfileUpdateDTO source, Profile destination) {
        BeanUtils.copyProperties(source, destination, getNullFieldNames(source));
        return destination;
    }

    public ProfileResponseDTO mapProfileToProfileResponseDTO(Profile profile) {
        List<FilmPreviewResponseDTO> starring = mapFilmToFilmInProfileResponseDTO(profile.getStarringFilms());
        List<FilmPreviewResponseDTO> directed = mapFilmToFilmInProfileResponseDTO(profile.getDirectedFilms());
        return new ProfileResponseDTO(
                profile.getId(),
                profile.getName(),
                profile.getBiography(),
                starring,
                directed,
                profile.getUrl()
        );
    }

    private List<FilmPreviewResponseDTO> mapFilmToFilmInProfileResponseDTO(List<Film> films) {
        if (films == null) {
            return null;
        }
        return films.stream()
                .map(this::mapFilmToFilmInProfileResponseDTO)
                .toList();
    }

    private FilmPreviewResponseDTO mapFilmToFilmInProfileResponseDTO(Film film) {
        if (film == null) {
            return null;
        }
        return new FilmPreviewResponseDTO(film.getId(), film.getTitle(), film.getUrl());
    }

    public List<ProfileResponseDTO> mapProfileToProfileResponseDTO(List<Profile> profiles) {
        if (profiles == null) {
            return null;
        }

        return profiles.stream()
                .map(this::mapProfileToProfileResponseDTO)
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
