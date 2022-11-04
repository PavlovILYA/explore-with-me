package ru.practicum.explore.with.me.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.with.me.event.model.Event;
import ru.practicum.explore.with.me.request.model.ParticipationRequest;
import ru.practicum.explore.with.me.user.model.User;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByRequesterId(Long requesterId);

    Optional<ParticipationRequest> findByEventAndRequester(Event event, User requester);

    List<ParticipationRequest> findAllByEvent(Event event);

    @Modifying
    @Query("UPDATE ParticipationRequest AS r " +
            "SET r.status = 'REJECTED' " +
            "WHERE r.status = 'PENDING' AND r.event.id = :eventId")
    void rejectAllPendingRequests(Long eventId);
}
