package ru.practicum.explore.with.me.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.with.me.request.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByRequesterId(Long requesterId);

    Optional<ParticipationRequest> findByIdAndRequesterId(Long id, Long requesterId);
}
