package com.joyjoin.eventservice.repository;
import com.joyjoin.eventservice.model.EventParticipationCount;
import com.joyjoin.eventservice.model.Tag;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import com.joyjoin.eventservice.model.Event;

import java.time.LocalDate;
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

    public static Specification<Event> isAtDate(LocalDate date) {
        if (date == null) return null;
        return (root, query, criteriaBuilder) -> {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);
            return criteriaBuilder.between(root.get("time"), startOfDay, endOfDay);
        };
    }

    public static Specification<Event> hasTags(List<String> tags) {
        return (root, query, cb) -> {
            if (tags == null || tags.isEmpty()) {
                return cb.conjunction();
            }
            Expression<String> tagsExpression = root.get("tags");
            Predicate allTagsMatch = cb.conjunction();
            for (String tag : tags) {
                allTagsMatch = cb.and(allTagsMatch, cb.like(tagsExpression, "%" + tag.trim() + "%"));
            }
            return allTagsMatch;
        };
    }



    public static Specification<Event> participationLimitNotReached() {
        return (root, query, criteriaBuilder) -> {
            // 关联事件与参与人数的表
            Subquery<Integer> participationSubquery = query.subquery(Integer.class);
            Root<EventParticipationCount> participationRoot = participationSubquery.from(EventParticipationCount.class);
            participationSubquery.select(participationRoot.get("participantCount"))
                    .where(criteriaBuilder.equal(participationRoot.get("eventId"), root.get("eventId")));

            // 比较参与人数是否小于限制
            return criteriaBuilder.lessThan(participationSubquery, root.get("participationLimit"));
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
