package com.seyan.film.film;

import com.seyan.film.activity.ActivityClient;
import com.seyan.film.activity.ActivityOnFilmResponseDTO;
import com.seyan.film.dto.film.FilmCreationDTO;
import com.seyan.film.dto.film.FilmMapper;
import com.seyan.film.dto.film.FilmUpdateDTO;
import com.seyan.film.exception.FilmNotFoundException;
import com.seyan.film.exception.ProfileNotFoundException;
import com.seyan.film.exception.SortingParametersException;
import com.seyan.film.profile.Profile;
import com.seyan.film.profile.ProfileRepository;
import com.seyan.film.review.ReviewClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class FilmService {
    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;
    private final ProfileRepository profileRepository;
    private final ActivityClient activityClient;
    private final ReviewClient reviewClient;

    public Film createFilm(FilmCreationDTO dto) {
        Film film = filmMapper.mapFilmCreationDTOToFilm(dto);

        if (dto.directorId() != null) {
            Profile director = profileRepository.findById(dto.directorId()).orElseThrow(() -> new ProfileNotFoundException(
                    String.format("No director profile found with the provided ID: %s", dto.directorId())
            ));
            film.setDirector(director);
        }

        if (dto.castIdList() != null) {
            List<Profile> foundInDatabaseCastList = profileRepository.findAllById(dto.castIdList());
            if (dto.castIdList().size() != foundInDatabaseCastList.size()) {
                List<Long> foundCastIdList = foundInDatabaseCastList.stream().map(Profile::getId).toList();
                dto.castIdList().removeAll(foundCastIdList);
                throw new ProfileNotFoundException(
                        String.format("Some profiles from the provided ID list were not found : %s", dto.castIdList()));
            }
            film.getCast().addAll(foundInDatabaseCastList);
        }

        Film withUrl = createUrl(film);
        return filmRepository.save(withUrl);
    }

    private Film createUrl(Film film) {
        String[] title = film.getTitle().toLowerCase().split(" ");
        StringBuilder urlBuilder = new StringBuilder();

        for (String s : title) {
            urlBuilder.append(s).append("-");
        }

        String url = urlBuilder.append(film.getReleaseDate().getYear()).toString();
        film.setUrl(url);

        int similarTitleCount = filmRepository.countByTitleIgnoreCase(film.getTitle());

        if (similarTitleCount > 0) {
            urlBuilder.append("-").append(++similarTitleCount);
            film.setUrl(urlBuilder.toString());
            return film;
        }

        return film;
    }

    public Film getFilmById(Long id) {
        return filmRepository.findById(id).orElseThrow(() -> new FilmNotFoundException(
                String.format("No film found with the provided ID: %s", id)));
    }

    public List<Film> getFilmsByIdList(List<Long> filmIds) {
        return filmRepository.findAllById(filmIds);
    }

    public Film updateFilm(FilmUpdateDTO dto, Long id) {
        Film film = filmRepository.findById(id).orElseThrow(() -> new FilmNotFoundException(
                String.format("No film found with the provided ID: %s", id)));

        Film mapped = filmMapper.mapFilmUpdateDTOToFilm(dto, film);

        if (dto.title() != null && !dto.title().equals(film.getTitle())) {
            Film mappedWithUrl = createUrl(mapped);
            return filmRepository.save(mappedWithUrl);
        }

        return filmRepository.save(mapped);
    }

    public Film addCastMember(List<Long> profileIdList, Long filmId) {
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new FilmNotFoundException(
                String.format("No film found with the provided ID: %s", filmId)));

        List<Profile> profileList = profileRepository.findAllById(profileIdList);

        if (profileIdList.size() != profileList.size()) {
            List<Long> foundProfileIdList = profileList.stream().map(Profile::getId).toList();
            profileIdList.removeAll(foundProfileIdList);
            throw new ProfileNotFoundException(
                    String.format("Some profiles from the provided ID list were not found : %s", profileIdList));
        }

        film.getCast().addAll(profileList);
        return filmRepository.save(film);
    }

    public Film removeCastMember(List<Long> profileIdList, Long filmId) {
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new FilmNotFoundException(
                String.format("No film found with the provided ID: %s", filmId)));

        List<Profile> profileList = profileRepository.findAllById(profileIdList);

        profileList.forEach(film.getCast()::remove);
        return filmRepository.save(film);
    }

    public Film updateDirector(Long profileId, Long filmId) {
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new FilmNotFoundException(
                String.format("No film found with the provided ID: %s", filmId)));

        Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new ProfileNotFoundException(
                String.format("No profile found with the provided ID: %s", profileId)
        ));

        film.setDirector(profile);

        return filmRepository.save(film);
    }

    public Film updateWatchedCount(Long filmId, boolean toAdd) {
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new FilmNotFoundException(
                String.format("No film found with the provided ID: %s", filmId)));

        long updatedWatchedCount;
        if (toAdd) {
            updatedWatchedCount = film.getWatchedCount() + 1;
        } else {
            updatedWatchedCount = film.getWatchedCount() - 1;
        }

        film.setWatchedCount(updatedWatchedCount);
        return filmRepository.save(film);
    }

    public Film updateLikeCount(Long filmId, boolean toAdd) {
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new FilmNotFoundException(
                String.format("No film found with the provided ID: %s", filmId)));

        long updatedLikeCount;
        if (toAdd) {
            updatedLikeCount = film.getLikeCount() + 1;
        } else {
            updatedLikeCount = film.getLikeCount() - 1;
        }

        film.setLikeCount(updatedLikeCount);
        return filmRepository.save(film);
    }

    public Film updateAvgRating(Long filmId, Double rating) {
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new FilmNotFoundException(
                String.format("No film found with the provided ID: %s", filmId)));

        film.setAvgRating(rating);
        return filmRepository.save(film);
    }

    public void deleteFilm(Long id) {
        filmRepository.findById(id).orElseThrow(() -> new FilmNotFoundException(
                String.format("No film found with the provided ID: %s", id)));

        filmRepository.deleteById(id);
    }

    public List<Film> getAllFilmsByTitle(String title) {

        String[] split = title.split("-");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < split.length - 1; i++) {
            builder.append(split[i]).append(" ");
        }
        String parsedTitle = builder.append(split[split.length - 1]).toString();

        return filmRepository.findByTitleContaining(parsedTitle);
    }

    public Film getFilmByUrl(String filmUrl) {
        return filmRepository.findByUrl(filmUrl).orElseThrow(() -> new FilmNotFoundException(
                String.format("No film found with the provided url: %s", filmUrl)
        ));
    }

    public List<Film> getAllFilmsWithParams(Map<String, String> params, Long userId) {
        Stream<Film> stream;

        if (params.containsKey("popular")) {
            stream = filterFilmsByPopularity(params.get("popular"));
        } else {
            stream = filmRepository.findAll().stream();
        }

        if (params.containsKey("decade")) {
            stream = filterFilmsByDecade(stream, params.get("decade"));
        }

        if (params.containsKey("year")) {
            stream = filterFilmsByYear(stream, params.get("year"));
        }

        if (params.containsKey("genre")) {
            stream = filterFilmsByGenre(stream, params.get("genre"));
        }

        if (params.containsKey("name")) {
            stream = filterFilmsByName(stream, params.get("name"));
        }

        if (params.containsKey("release")) {
            stream = filterFilmsByReleaseDate(stream, params.get("release"));
        }

        if (params.containsKey("rating")) {
            stream = filterFilmsByRating(stream, params.get("rating"));
        }

        if (params.containsKey("your-rating")) {
            stream = filterFilmsByYourRating(stream, params.get("your-rating"), userId);
        }

        if (params.containsKey("length")) {
            stream = filterFilmsByLength(stream, params.get("length"));
        }

        return stream.toList();
    }

    private Stream<Film> filterFilmsByName(Stream<Film> stream, String name) {
        switch (name) {
            case "asc" -> {
                return stream.sorted(Comparator.comparing(Film::getTitle));
            }
            case "desc" -> {
                return stream.sorted(Comparator.comparing(Film::getTitle).reversed());
            }
            default -> {
                throw new SortingParametersException(
                        "Could not parse name parameter, should be \"asc\" or \"desc\"");
            }
        }
    }

    private Stream<Film> filterFilmsByLength(Stream<Film> stream, String length) {
        switch (length) {
            case "shortest" -> {
                return stream.sorted(Comparator.comparing(Film::getRunningTimeMinutes));
            }
            case "longest" -> {
                return stream.sorted(Comparator.comparing(Film::getRunningTimeMinutes).reversed());
            }
            default -> {
                throw new SortingParametersException(
                        "Could not parse length parameter, should be \"shortest\" or \"longest\"");
            }
        }
    }

    private Stream<Film> filterFilmsByYourRating(Stream<Film> stream, String rating, Long userId) {
        List<ActivityOnFilmResponseDTO> activityList = activityClient.getActivityByUserIdAndByRatingGreaterThan(userId, 0.0).getData();
        Map<Long, Double> filmIdAndRatingList = activityList.stream().collect(Collectors.toMap(it -> it.id().getFilmId(), ActivityOnFilmResponseDTO::rating));

        Map<Boolean, List<Film>> filmLists = stream
                .collect(Collectors.partitioningBy(film -> filmIdAndRatingList.containsKey(film.getId())));

        List<Film> filmsWithRating = filmLists.get(true);

        List<Film> filmsWithoutRating = filmLists.get(false);

        switch (rating) {
            case "highest" -> {
                List<Film> withRatingSorted = new java.util.ArrayList<>(
                        filmsWithRating.stream().sorted(Comparator.comparingDouble(o -> filmIdAndRatingList.get(o.getId()))).toList());
                Collections.reverse(withRatingSorted);
                return Stream.concat(
                        withRatingSorted.stream(),
                        filmsWithoutRating.stream().sorted(Comparator.comparingDouble(Film::getAvgRating).reversed()));
            }
            case "lowest" -> {
                return Stream.concat(
                        filmsWithRating.stream().sorted(Comparator.comparing(o -> filmIdAndRatingList.get(o.getId()))),
                        filmsWithoutRating.stream().sorted(Comparator.comparingDouble(Film::getAvgRating)));
            }
            default -> {
                throw new SortingParametersException(
                        "Could not parse rating parameter, should be \"highest\" or \"lowest\"");
            }
        }
    }

    private Stream<Film> filterFilmsByGenre(Stream<Film> stream, String genre) {
        return stream.filter(it -> it.getGenre().equals(Genre.valueOf(genre)));
    }

    private Stream<Film> filterFilmsByDecade(Stream<Film> stream, String decade) {
        if (decade.equals("upcoming")) {
            LocalDate now = LocalDate.now();
            return stream.filter(it -> it.getReleaseDate().isAfter(now));
        }

        int decadeParsed = Integer.parseInt(decade);
        return stream.filter(it -> it.getReleaseDate().getYear() >= decadeParsed && it.getReleaseDate().getYear() < decadeParsed + 10);
    }

    private Stream<Film> filterFilmsByYear(Stream<Film> stream, String year) {
        if (year.equals("upcoming")) {
            LocalDate now = LocalDate.now();
            return stream.filter(it -> it.getReleaseDate().isAfter(now));
        }

        int yearParsed = Integer.parseInt(year);
        return stream.filter(it -> it.getReleaseDate().getYear() == yearParsed);
    }

    private Stream<Film> filterFilmsByPopularity(String popularity) {
        switch (popularity) {
            case "all-time" -> {
                return getFilmsBasedOnIdRepetitiveness().stream();
            }
            case "this-year" -> {
                return getFilmsBasedOnReviewDateAfter(LocalDate.now().minusYears(1)).stream();
            }
            case "this-month" -> {
                return getFilmsBasedOnReviewDateAfter(LocalDate.now().minusMonths(1)).stream();
            }
            case "this-week" -> {
                return getFilmsBasedOnReviewDateAfter(LocalDate.now().minusDays(7)).stream();
            }
            default -> {
                throw new SortingParametersException(
                        "Could not parse popularity parameter, should be \"all-time\", \"this-month\", \"this-week\" or \"this-year\"");
            }
        }
    }

    private List<Film> getFilmsBasedOnReviewDateAfter(LocalDate date) {
        List<Long> filmIdList = reviewClient.getFilmIdsBasedOnReviewDateAfter(date).getData();
        List<Long> filmIdListSorted = filmIdList.stream().sorted(Comparator.comparing(it -> Collections.frequency(filmIdList, it)).reversed()).distinct().toList();

        List<Film> filmList = filmRepository.findAllById(filmIdListSorted);
        filmList.sort(Comparator.comparing(it -> filmIdListSorted.indexOf(it.getId())));

        return filmList;
    }

    private List<Film> getFilmsBasedOnIdRepetitiveness() {
        List<Long> filmIdList = reviewClient.getAllFilmIds().getData();
        List<Long> filmIdListSorted = filmIdList.stream().sorted(Comparator.comparing(it -> Collections.frequency(filmIdList, it)).reversed()).distinct().toList();

        List<Film> filmList = filmRepository.findAllById(filmIdListSorted);
        filmList.sort(Comparator.comparing(it -> filmIdListSorted.indexOf(it.getId())));

        return filmList;
    }

    private Stream<Film> filterFilmsByReleaseDate(Stream<Film> stream, String release) {
        switch (release) {
            case "newest" -> {
                return stream.sorted(Comparator.comparing(Film::getReleaseDate).reversed());
            }
            case "earliest" -> {
                return stream.sorted(Comparator.comparing(Film::getReleaseDate));
            }
            default -> {
                throw new SortingParametersException(
                        "Could not parse release parameter, should be \"earliest\" or \"newest\"");
            }
        }
    }

    private Stream<Film> filterFilmsByRating(Stream<Film> stream, String rating) {
        switch (rating) {
            case "highest" -> {
                return stream.sorted(Comparator.comparing(Film::getAvgRating).reversed());
            }
            case "lowest" -> {
                return stream.sorted(Comparator.comparing(Film::getAvgRating));
            }
            default -> {
                throw new SortingParametersException(
                        "Could not parse rating parameter, should be \"highest\" or \"lowest\"");
            }
        }
    }
}
