package ru.practicum.explore.with.me.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.with.me.event.model.Event;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findEventByIdAndInitiatorId(Long eventId, Long initiatorId);

    Page<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);
}