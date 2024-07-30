package com.seyan.film.profile;

import com.seyan.reviewmonolith.exception.film.FilmNotFoundException;
import com.seyan.reviewmonolith.exception.profile.ProfileNotFoundException;
import com.seyan.reviewmonolith.film.Film;
import com.seyan.reviewmonolith.film.FilmRepository;
import com.seyan.reviewmonolith.film.FilmService;
import com.seyan.reviewmonolith.profile.dto.ProfileCreationDTO;
import com.seyan.reviewmonolith.profile.dto.ProfileMapper;
import com.seyan.reviewmonolith.profile.dto.ProfileUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final FilmService filmService;
    private final FilmRepository filmRepository;

    //todo create with starring and directed films
    @Transactional
    public Profile createProfile(ProfileCreationDTO dto) {
        Profile profile = profileMapper.mapPofileCreationDTOToProfile(dto);

        Profile withUrl = createUrl(profile);
        profileRepository.save(withUrl);

        if (dto.starringFilmsId() != null) {
            addStarringFilm(profile.getId(), dto.starringFilmsId());
        }

        if (dto.directedFilmsId() != null) {
            addDirectedFilm(profile.getId(), dto.directedFilmsId());
        }

        return profile;
    }

    private Profile createUrl(Profile profile) {
        //todo fix count when profiles were deleted
        String[] name = profile.getName().toLowerCase().split(" ");
        StringBuilder urlBuilder = new StringBuilder();

        for (int i = 0; i <name.length - 1; i++) {
            urlBuilder.append(name[i]).append("-");
        }
        urlBuilder.append(name[name.length - 1]);

        String url = urlBuilder.toString();
        profile.setUrl(url);

        int similarNameCount = profileRepository.countByNameIgnoreCase(profile.getName());

        if (similarNameCount > 0) {
            urlBuilder.append("-").append(++similarNameCount);
            profile.setUrl(urlBuilder.toString());
            return profile;
        }

        return profile;
    }

    public Profile getProfileByUrl(String profileUrl) {
        //todo change throw msg???
        return profileRepository.findByUrl(profileUrl).orElseThrow(() -> new ProfileNotFoundException(
                String.format("No profile found with the provided url: %s", profileUrl)
        ));
    }

    public Profile getProfileById(Long id) {
        return profileRepository.findById(id).orElseThrow(() -> new ProfileNotFoundException(
                String.format("No profile found with the provided ID: %s", id)
        ));
    }

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Profile updateProfile(ProfileUpdateDTO dto, Long id) {
        Profile profile = profileRepository.findById(id).orElseThrow(() -> new ProfileNotFoundException(
                String.format("No profile found with the provided ID: %s", id)
        ));
        Profile mapped = profileMapper.mapProfileUpdateDTOToProfile(dto, profile);
        return profileRepository.save(mapped);
    }

    @Transactional
    public void deleteProfile(Long id) {
        profileRepository.findById(id).orElseThrow(() -> new ProfileNotFoundException(
                String.format("Cannot delete profile:: No profile found with the provided ID: %s", id)));
        profileRepository.deleteRelation(id);
        profileRepository.deleteById(id);
    }

    //todo get all by name containing
    public List<Profile> getAllProfilesByName(String name) {
        String[] split = name.split("-");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < split.length - 1; i++) {
            builder.append(split[i]).append(" ");
        }
        String parsedTitle = builder.append(split[split.length - 1]).toString();
        return profileRepository.findByNameContaining(name);
    }

    //todo get all by film id
    /*public List<Profile> getAllProfilesByFilmId(Long id) {

    }*/

    /*public Profile addStarringFilm(Long profileId, Long filmId) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(
                String.format("No profile found with the provided ID: %s", profileId)
        ));

        Film film = filmRepository.findById(filmId).orElseThrow(() -> new FilmNotFoundException(
                String.format("No film found with the provided ID: %s", filmId)));

        film.getCast().add(profile);
        filmRepository.save(film);
        return profile;
    }*/

    public Profile addStarringFilm(Long profileId, List<Long> filmIdList) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(
                String.format("No profile found with the provided ID: %s", profileId)
        ));

        List<Film> filmList = filmRepository.findAllById(filmIdList);

        if (filmIdList.size() != filmList.size()) {
            List<Long> foundFilmIdList = filmList.stream().map(Film::getId).toList();
            filmIdList.removeAll(foundFilmIdList);
            throw new FilmNotFoundException(
                    String.format("Some films from the provided ID list were not found : %s", filmIdList));
        }

        filmList.forEach(film -> film.getCast().add(profile));
        filmRepository.saveAll(filmList);
        return profile;
    }

    /*public Profile removeStarringFilm(Long profileId, Long filmId) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(
                String.format("No profile found with the provided ID: %s", profileId)
        ));

        Film film = filmRepository.findById(filmId).orElseThrow(() -> new FilmNotFoundException(
                String.format("No film found with the provided ID: %s", filmId)));

        film.getCast().remove(profile);
        filmRepository.save(film);
        return profile;
    }*/

    public Profile removeStarringFilm(Long profileId, List<Long> filmIdList) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(
                String.format("No profile found with the provided ID: %s", profileId)
        ));

        List<Film> filmList = filmRepository.findAllById(filmIdList);

        filmList.forEach(film -> film.getCast().remove(profile));
        filmRepository.saveAll(filmList);
        return profile;
    }

    /*public Profile addDirectedFilm(Long profileId, Long filmId) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(
                String.format("No profile found with the provided ID: %s", profileId)
        ));

        Film film = filmRepository.findById(filmId).orElseThrow(() -> new FilmNotFoundException(
                String.format("No film found with the provided ID: %s", filmId)));

        film.setDirector(profile);
        filmRepository.save(film);
        return profile;
    }*/

    public Profile addDirectedFilm(Long profileId, List<Long> filmIdList) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(
                String.format("No profile found with the provided ID: %s", profileId)
        ));

        List<Film> filmList = filmRepository.findAllById(filmIdList);

        if (filmIdList.size() != filmList.size()) {
            List<Long> foundFilmIdList = filmList.stream().map(Film::getId).toList();
            filmIdList.removeAll(foundFilmIdList);
            throw new FilmNotFoundException(
                    String.format("Some films from the provided ID list were not found : %s", filmIdList));
        }

        filmList.forEach(film -> film.setDirector(profile));
        filmRepository.saveAll(filmList);
        return profile;
    }

    //todo is it a good practice to set null?
    //todo test reversed OneToMany relation
    /*public Profile removeDirectedFilm(Long profileId, Long filmId) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(
                String.format("No profile found with the provided ID: %s", profileId)
        ));

        Film film = filmRepository.findById(filmId).orElseThrow(() -> new FilmNotFoundException(
                String.format("No film found with the provided ID: %s", filmId)));

        //film.getCast().remove(profile);
        film.setDirector(null);
        filmRepository.save(film);
        return profile;
    }*/

    public Profile removeDirectedFilm(Long profileId, List<Long> filmIdList) {
        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(
                String.format("No profile found with the provided ID: %s", profileId)
        ));

        List<Film> filmList = filmRepository.findAllById(filmIdList);

        //filmList.forEach(film -> film.getCast().remove(profile));
        filmList.forEach(film -> film.setDirector(null));
        filmRepository.saveAll(filmList);
        return profile;
    }
}
