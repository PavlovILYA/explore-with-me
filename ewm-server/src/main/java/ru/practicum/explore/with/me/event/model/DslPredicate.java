package ru.practicum.explore.with.me.event.model;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DslPredicate {
    private final List<Predicate> predicates = new ArrayList<>();

    public static DslPredicate builder() {
        return new DslPredicate();
    }

    public <T> DslPredicate addPredicate(T object, Function<T, Predicate> function) {
        if (object != null) {
            predicates.add(function.apply(object));
        }
        return this;
    }

    public <T> DslPredicate addListPredicate(List<? extends T> list, Function<List<? extends T>, Predicate> function) {
        if (list != null && !list.isEmpty()) {
            predicates.add(function.apply(list));
        }
        return this;
    }

    public Predicate build() {
        return ExpressionUtils.allOf(predicates);
    }
}
