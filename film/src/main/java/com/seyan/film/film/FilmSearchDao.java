package com.seyan.film.film;

import com.seyan.film.exception.SortingParametersException;
import com.seyan.film.review.ReviewClient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class FilmSearchDao {
    private final EntityManager entityManager;

    public Page<Film> findByDecadeAndGenreAndSorting(String decade, String genre, String sorting, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Film> criteriaQuery = builder.createQuery(Film.class);

        List<Predicate> predicates = new ArrayList<>();
        Root<Film> root = criteriaQuery.from(Film.class);

        if (decade != null) {
            addDecadePredicate(decade, predicates, root, builder);
        }
        if (genre !=null) {
            addGenrePredicate(genre, predicates, root, builder);
        }

        if (decade == null || genre == null) {
            criteriaQuery.where(predicates.get(0));
        } else {
            criteriaQuery.where(builder.and(predicates.get(0), predicates.get(1)));
        }

        Order order = addSortingPredicate(sorting, predicates, root, builder);
        criteriaQuery.orderBy(order);

        TypedQuery<Film> typedQuery = entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize());

        int totalRows = typedQuery.getResultList().size();

        return new PageImpl<Film>(typedQuery.getResultList(), pageable, totalRows);

    }

    //todo rename to length
    private Order addSortingPredicate(String sorting, List<Predicate> predicates, Root<Film> root, CriteriaBuilder builder) {
        switch (sorting) {
            case "name" -> {
                return builder.asc(root.get("title"));
            }
            case "release-newest" -> {
                return builder.desc(root.get("releaseDate"));
            }
            case "release-earliest" -> {
                return builder.asc(root.get("releaseDate"));
            }
            case "rating-highest" -> {
                return builder.desc(root.get("rating"));
            }
            case "rating-lowest" -> {
                return builder.asc(root.get("rating"));
            }
            case "length-shortest" -> {
                return builder.asc(root.get("runningTimeMinutes"));
            }
            case "length-longest" -> {
                return builder.desc(root.get("runningTimeMinutes"));
            }
            case "popularity-highest" -> {
                return builder.desc(root.get("reviewCount"));
            }
            case "popularity-lowest" -> {
                return builder.asc(root.get("reviewCount"));
            }
            default -> throw new SortingParametersException(
                    "Could not parse sorting parameter");
        }
    }

    //valid genre check
    private void addGenrePredicate(String genre, List<Predicate> predicates, Root<Film> root, CriteriaBuilder builder) {
        Predicate genrePredicate = builder.like(root.get("genre"), genre.toUpperCase());
        predicates.add(genrePredicate);
    }

    private void addDecadePredicate(String decade, List<Predicate> predicates, Root<Film> root, CriteriaBuilder builder) {
        LocalDate dateBefore;
        LocalDate dateAfter;
        switch (decade) {
            case "2020s" -> {
                dateBefore = LocalDate.of(2030, 1, 1);
                dateAfter = LocalDate.of(2019, 12, 31);
            }
            case "2010s" -> {
                dateBefore = LocalDate.of(2020, 1, 1);
                dateAfter = LocalDate.of(2009, 12, 31);
            }
            case "2000s" -> {
                dateBefore = LocalDate.of(2010, 1, 1);
                dateAfter = LocalDate.of(1999, 12, 31);
            }
            case "1990s" -> {
                dateBefore = LocalDate.of(2000, 1, 1);
                dateAfter = LocalDate.of(1989, 12, 31);
            }
            case "1980s" -> {
                dateBefore = LocalDate.of(1990, 1, 1);
                dateAfter = LocalDate.of(1979, 12, 31);
            }
            case "1970s" -> {
                dateBefore = LocalDate.of(1980, 1, 1);
                dateAfter = LocalDate.of(1969, 12, 31);
            }
            case "1960s" -> {
                dateBefore = LocalDate.of(1970, 1, 1);
                dateAfter = LocalDate.of(1959, 12, 31);
            }
            default -> {
                throw new SortingParametersException(
                        "Could not parse decade parameter, should be from \"1960\" to \"2020s\"");
            }
        }
        Predicate decadePredicate = builder.between(root.get("releaseDate"), dateBefore, dateAfter);
        predicates.add(decadePredicate);
    }
}
