package com.joyjoin.eventservice.repository;
import com.joyjoin.eventservice.model.Tag;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import com.joyjoin.eventservice.model.Event;

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
        return (root, query, cb) -> {
            if (tags == null || tags.isEmpty()) {
                return cb.conjunction(); // 如果没有标签过滤应用，则返回一个始终为真的谓词。
            }
            Expression<String> tagsExpression = root.get("tags"); // 假设 tags 是一个以逗号分隔的字符串字段
            Predicate allTagsMatch = cb.conjunction();
            for (String tag : tags) {
                allTagsMatch = cb.and(allTagsMatch, cb.like(tagsExpression, "%" + tag.trim() + "%"));
            }
            return allTagsMatch;
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
