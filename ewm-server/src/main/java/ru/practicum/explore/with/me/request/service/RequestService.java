package ru.practicum.explore.with.me.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore.with.me.event.exception.EventNotFoundException;
import ru.practicum.explore.with.me.event.model.Event;
import ru.practicum.explore.with.me.event.model.EventState;
import ru.practicum.explore.with.me.event.repository.EventRepository;
import ru.practicum.explore.with.me.request.exception.RequestNotFoundException;
import ru.practicum.explore.with.me.request.exception.RequestValidationException;
import ru.practicum.explore.with.me.request.model.ParticipationRequest;
import ru.practicum.explore.with.me.request.model.RequestStatus;
import ru.practicum.explore.with.me.request.repository.RequestRepository;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    public ParticipationRequest saveRequest(ParticipationRequest request) {
        validateRequestBeforeSaving(request);
        ParticipationRequest savedRequest = requestRepository.save(request);
        log.info("Saved request: {}", savedRequest);
        return savedRequest;
    }

    public List<ParticipationRequest> getMyRequests(Long userId) {
        List<ParticipationRequest> requests = requestRepository.findAllByRequesterId(userId);
        log.info("All requests: {}", requests);
        return requests;
    }

    public ParticipationRequest cancelRequest(Long userId, Long requestId) {
        ParticipationRequest request = getRequest(requestId);
        validateRequestAndRequester(request, userId);
        request.setStatus(RequestStatus.CANCELED);
        ParticipationRequest canceledRequest = requestRepository.save(request);
        log.info("Canceled request: {}", canceledRequest);
        return canceledRequest;
    }

    public List<ParticipationRequest> getRequestsToMyEvent(Long userId, Long eventId) {
        Event event = eventRepository.findEventByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        List<ParticipationRequest> requests = requestRepository.findAllByEvent(event);
        log.info("Requests to event {}: {}", eventId, requests);
        return requests;
    }

    public ParticipationRequest confirmRequestToMyEvent(Long userId, Long eventId, Long requestId) {
        ParticipationRequest request = getRequest(requestId);
        validateRequestAndEventInitiator(request, userId, eventId);
        checkParticipantLimit(request);
        request.setStatus(RequestStatus.CONFIRMED);
        ParticipationRequest confirmedRequest = requestRepository.save(request);
        log.info("Confirmed request: {}", confirmedRequest);
        rejectAllTheRestIfLimitReached(request.getEvent());
        return confirmedRequest;
    }

    public ParticipationRequest rejectRequestToMyEvent(Long userId, Long eventId, Long requestId) {
        ParticipationRequest request = getRequest(requestId);
        validateRequestAndEventInitiator(request, userId, eventId);
        request.setStatus(RequestStatus.REJECTED);
        ParticipationRequest rejectedRequest = requestRepository.save(request);
        log.info("Rejected request: {}", rejectedRequest);
        return rejectedRequest;
    }

    private ParticipationRequest getRequest(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException(requestId));
    }

    private void validateRequestAndRequester(ParticipationRequest request, Long requesterId) {
        if (!Objects.equals(request.getRequester().getId(), requesterId)) {
            throw new RequestValidationException("Wrong requester");
        }
    }

    private void validateRequestAndEventInitiator(ParticipationRequest request, Long initiatorId, Long eventId) {
        if (!Objects.equals(request.getEvent().getId(), eventId)
                || !Objects.equals(request.getEvent().getInitiator().getId(), initiatorId)) {
            throw new RequestValidationException("Wrong event initiator");
        }
    }

    private void checkParticipantLimit(ParticipationRequest request) {
        if (isLimitReached(request.getEvent())) {
            throw new RequestValidationException("Cannot save/confirm same request – limit is already reached");
        }
    }

    private void rejectAllTheRestIfLimitReached(Event event) {
        if (isLimitReached(event)) {
            log.info("reject all pending requests");
            requestRepository.rejectAllPendingRequests(event.getId());
        }
    }

    private void validateRequestBeforeSaving(ParticipationRequest request) {
        // нельзя добавить повторный запрос
        if (requestRepository.findByEventAndRequester(request.getEvent(), request.getRequester()).isPresent()) {
            throw new RequestValidationException("Cannot save same request");
        }
        // нельзя участвовать в неопубликованном событии
        if (!request.getEvent().getState().equals(EventState.PUBLISHED)) {
            throw new RequestValidationException("Cannot save request to not published event");
        }
        // инициатор события не может добавить запрос на участие в своём событии
        if (Objects.equals(request.getRequester().getId(),
                request.getEvent().getInitiator().getId())) {
            throw new RequestValidationException("Initiator cannot save request to his own event");
        }
        // если у события достигнут лимит запросов на участие - необходимо вернуть ошибку
        checkParticipantLimit(request);
        // если для события отключена пре-модерация запросов на участие,
        // то запрос должен автоматически перейти в состояние подтвержденного
        if (!request.getEvent().getRequestModeration() || request.getEvent().getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        }
    }

    private Boolean isLimitReached(Event event) {
        Integer confirmedRequestAmount = eventRepository.getConfirmedRequestAmount(event.getId());
        return event.getParticipantLimit() != 0
                && Objects.equals(event.getParticipantLimit(), confirmedRequestAmount);
    }
}
