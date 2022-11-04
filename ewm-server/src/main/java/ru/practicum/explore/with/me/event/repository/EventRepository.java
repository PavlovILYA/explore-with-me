package ru.practicum.explore.with.me.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.explore.with.me.event.model.Event;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    Optional<Event> findEventByIdAndInitiatorId(Long eventId, Long initiatorId);

    Page<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    @Query("SELECT COUNT(r.id) FROM Event AS e " +
            "JOIN ParticipationRequest as r ON e.id = r.event.id " +
            "WHERE e.id = :eventId AND r.status = 'CONFIRMED'")
    Integer getConfirmedRequestAmount(Long eventId);
}