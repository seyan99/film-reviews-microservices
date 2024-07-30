package com.seyan.film.profile.dto;


import com.seyan.reviewmonolith.film.Film;
import com.seyan.reviewmonolith.profile.Profile;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ProfileMapper {
    public Profile mapPofileCreationDTOToProfile(ProfileCreationDTO dto) {
        return Profile.builder()
                .name(dto.name())
                .biography(dto.biography())
                // starring and directed wouldn't update from profile entity
                //.starringFilms(dto.starringFilms())
                //.directedFilms(dto.directedFilms())
                .build();
    }

    public Profile mapProfileUpdateDTOToProfile(ProfileUpdateDTO source, Profile destination) {
        BeanUtils.copyProperties(source, destination, getNullFieldNames(source));
        return destination;
    }

    public ProfileResponseDTO mapProfileToProfileResponseDTO(Profile profile) {
        List<FilmInProfileResponseDTO> starring = mapFilmToFilmInProfileResponseDTO(profile.getStarringFilms());
        List<FilmInProfileResponseDTO> directed = mapFilmToFilmInProfileResponseDTO(profile.getDirectedFilms());
        return new ProfileResponseDTO(
                profile.getId(),
                profile.getName(),
                profile.getBiography(),
                starring,
                directed,
                profile.getUrl()
        );
    }

    private List<FilmInProfileResponseDTO> mapFilmToFilmInProfileResponseDTO(List<Film> films) {
        if (films == null) {
            return null;
        }
        return films.stream()
                .map(this::mapFilmToFilmInProfileResponseDTO)
                .toList();
    }

    private FilmInProfileResponseDTO mapFilmToFilmInProfileResponseDTO(Film film) {
        if (film == null) {
            return null;
        }
        return new FilmInProfileResponseDTO(film.getId(), film.getTitle(), film.getUrl());
    }



    /*public PageableUserResponseDTO mapUsersPageToPageableUserResponseDTO(Page<User> usersPage) {
        List<User> listOfUsers = usersPage.getContent();
        List<UserProfileResponseDTO> userProfileResponseDTO = mapUserToUserProfileResponseDTO(listOfUsers);

        return PageableUserResponseDTO.builder()
                .content(userProfileResponseDTO)
                .pageNo(usersPage.getNumber())
                .pageSize(usersPage.getSize())
                .totalElements(usersPage.getTotalElements())
                .totalPages(usersPage.getTotalPages())
                .last(usersPage.isLast()).build();
    }*/

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
