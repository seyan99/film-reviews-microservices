package com.seyan.film.film;

import com.seyan.film.external.activity.ActivityClient;
import com.seyan.film.external.activity.ActivityOnFilmId;
import com.seyan.film.external.activity.ActivityOnFilmResponseDTO;
import com.seyan.film.dto.film.FilmCreationDTO;
import com.seyan.film.dto.film.FilmMapper;
import com.seyan.film.dto.film.FilmUpdateDTO;
import com.seyan.film.exception.FilmNotFoundException;
import com.seyan.film.exception.ProfileNotFoundException;
import com.seyan.film.profile.Profile;
import com.seyan.film.profile.ProfileRepository;
import com.seyan.film.external.review.ReviewClient;
import com.seyan.film.external.review.ReviewResponseDTO;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class FilmService {
    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;
    private final ProfileRepository profileRepository;
    private final ActivityClient activityClient;
    private final ReviewClient reviewClient;
    private final FilmSearchDao filmSearchDao;

    @RateLimiter(name = "responseBreaker", fallbackMethod = "reviewFallback")
    public Map<String, List<ReviewResponseDTO>> getLatestAndPopularReviewsForFilm(Long filmId) {
        return reviewClient.latestAndPopularReviewsForFilm(filmId).getData();
    }

    public Map<String, List<ReviewResponseDTO>> reviewFallback(Exception e) {
        Map<String, List<ReviewResponseDTO>> reviewFallback = new HashMap<>();
        reviewFallback.put("latest", Collections.emptyList());
        reviewFallback.put("popular", Collections.emptyList());
        return reviewFallback;
    }

    @RateLimiter(name = "responseBreaker", fallbackMethod = "activityFallback")
    public ActivityOnFilmResponseDTO getFilmActivity(Long userId, Long filmId) {
        return activityClient.getFilmActivity(userId, filmId).getData();
    }

    public ActivityOnFilmResponseDTO activityFallback(Exception e) {
        return ActivityOnFilmResponseDTO.builder()
                .id(new ActivityOnFilmId(0L, 0L))
                .isWatched(false)
                .isLiked(false)
                .isInWatchlist(false)
                .rating(0.0)
                .hasReview(false)
                .build();
    }

    public Page<Film> getFilmsFromList(List<Long> filmIds, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 20);
        return filmRepository.findAllByIdIn(filmIds, pageable);
    }

    public List<Film> getFilmsFromList(List<Long> filmIds) {
        List<Film> films = filmRepository.findAllById(filmIds);
        films.sort(Comparator.comparing(it -> filmIds.indexOf(it.getId())));
        return films;
    }

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

    public void updateAvgRating(Long filmId, Boolean toAdd) {
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new FilmNotFoundException(
                String.format("No film found with the provided ID: %s", filmId)));
        if (toAdd) {
            Double rating = activityClient.getAvgRating(filmId).getData();
            film.setAvgRating(rating);
            filmRepository.save(film);
        }
    }

    public void deleteFilm(Long id) {
        filmRepository.findById(id).orElseThrow(() -> new FilmNotFoundException(
                String.format("No film found with the provided ID: %s", id)));

        filmRepository.deleteById(id);
    }

    public Page<Film> getAllFilmsByTitle(String title, int pageNo) {

        String[] split = title.split("-");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < split.length - 1; i++) {
            builder.append(split[i]).append(" ");
        }
        String parsedTitle = builder.append(split[split.length - 1]).toString();

        Pageable pageable = PageRequest.of(pageNo - 1, 20);

        return filmRepository.findByTitleContainingIgnoreCase(parsedTitle, pageable);
    }

    public Film getFilmByUrl(String filmUrl) {
        return filmRepository.findByUrl(filmUrl).orElseThrow(() -> new FilmNotFoundException(
                String.format("No film found with the provided url: %s", filmUrl)
        ));
    }

    public Page<Film> getFilmsWithDecadeAndGenreAndSorting(String decade, String genre, String sorting, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 25);
        return filmSearchDao.findByDecadeAndGenreAndSorting(decade, genre, sorting, pageable);
    }

    public Film updateReviewCount(Long filmId, boolean add) {
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new FilmNotFoundException(
                String.format("No film found with the provided ID: %s", filmId)));

        if (add) {
            film.setReviewCount(film.getReviewCount() + 1);
        } else {
            film.setReviewCount(film.getReviewCount() - 1);
        }

        return filmRepository.save(film);
    }
}
