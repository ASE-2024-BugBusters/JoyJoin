package com.joyjoin.eventservice.repository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import com.joyjoin.eventservice.model.Event;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class EventSpecifications {

    private static Specification<Event> notDeletedAndNotExpired() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.isFalse(root.get("isDeleted")),
                criteriaBuilder.isFalse(root.get("isExpired"))
        );
    }

    public static Specification<Event> hasTitle(String title) {
        if (title == null || title.trim().isEmpty()) return null;
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Event> isInCity(String city) {
        if (city == null || city.trim().isEmpty()) return null;
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("location").get("city")), city.toLowerCase());
    }

    public static Specification<Event> isAtTime(LocalDateTime time) {
        if (time == null) return null;
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("time"), time);
    }

    public static Specification<Event> hasTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) return null;
        return (root, query, criteriaBuilder) -> {
            Predicate[] predicates = new Predicate[tags.size()];
            for (int i = 0; i < tags.size(); i++) {
                predicates[i] = criteriaBuilder.isMember(tags.get(i), root.get("tags"));
            }
            return criteriaBuilder.or(predicates);
        };
    }

    public static Specification<Event> participationLimitNotReached() {
        return (root, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Event> eventSubRoot = subquery.correlate(root);
            Join<Event, UUID> participants = eventSubRoot.join("participants");

            subquery.select(criteriaBuilder.count(participants.get("userId")))
                    .where(criteriaBuilder.equal(participants.get("isRegistered"), true));

            return criteriaBuilder.lessThan(subquery, root.get("participationLimit"));
        };
    }

    public static Specification<Event> combine(Specification<Event>... specs) {
        Specification<Event> result = Specification.where(notDeletedAndNotExpired());
        for (Specification<Event> spec : specs) {
            if (spec != null) {
                result = result.and(spec);
            }
        }
        return result;
    }
}
