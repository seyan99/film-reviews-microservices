package com.seyan.list.list;

import com.seyan.list.exception.SortingParametersException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@RequiredArgsConstructor
@Repository
public class ListSearchDao {
    private final EntityManager entityManager;

    public Page<List> findByUsernameAndSorting(String username, String sorting, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<List> criteriaQuery = builder.createQuery(List.class);

        java.util.List<Predicate> predicates = new ArrayList<>();
        Root<List> root = criteriaQuery.from(List.class);

        Order order;
        switch (sorting) {
            case "name" -> {
                order = builder.asc(root.get("title"));
            }
            case "likes-highest" -> {
                order = builder.desc(root.get("likedUsersIds"));
            }
            case "likes-lowest" -> {
                order = builder.asc(root.get("likedUsersIds"));
            }
            case "comments-highest" -> {
                order = builder.desc(root.get("commentIds"));
            }
            case "comments-lowest" -> {
                order = builder.asc(root.get("commentIds"));
            }
            case "newest" -> {
                order = builder.desc(root.get("creationDate"));
            }
            case "oldest" -> {
                order = builder.asc(root.get("creationDate"));
            }
            case "when-updated" -> {
                order = builder.desc(root.get("lastUpdateDate"));
            }
            default -> throw new SortingParametersException(
                    "Could not parse sorting parameter");
        }

        if (username != null) {
            criteriaQuery.where(builder.equal(root.get("username"), username));
        }

        criteriaQuery.orderBy(order);

        TypedQuery<List> typedQuery = entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize());

        int totalRows = typedQuery.getResultList().size();

        return new PageImpl<List>(typedQuery.getResultList(), pageable, totalRows);

    }
}
