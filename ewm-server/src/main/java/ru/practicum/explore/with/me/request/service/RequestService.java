package ru.practicum.explore.with.me.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore.with.me.request.exception.RequestNotFoundException;
import ru.practicum.explore.with.me.request.model.ParticipationRequest;
import ru.practicum.explore.with.me.request.model.RequestStatus;
import ru.practicum.explore.with.me.request.repository.RequestRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;

    public ParticipationRequest saveRequest(ParticipationRequest request) {
        ParticipationRequest savedRequest = requestRepository.save(request);
        log.info("Saved request: {}", savedRequest);
        return savedRequest;
    }

    public List<ParticipationRequest> getRequests(Long userId) {
        List<ParticipationRequest> requests = requestRepository.findAllByRequesterId(userId);
        log.info("All requests: {}", requests);
        return requests;
    }

    public ParticipationRequest cancelRequest(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new RequestNotFoundException(requestId));
        request.setStatus(RequestStatus.CANCELED);
        ParticipationRequest canceledRequest = requestRepository.save(request);
        log.info("Canceled request: {}", canceledRequest);
        return canceledRequest;
    }
}
