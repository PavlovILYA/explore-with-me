package ru.practicum.explore.with.me.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new RequestNotFoundException(requestId));
        request.setStatus(RequestStatus.CANCELED);
        ParticipationRequest canceledRequest = requestRepository.save(request);
        log.info("Canceled request: {}", canceledRequest);
        return canceledRequest;
    }

    private void validateRequestBeforeSaving(ParticipationRequest request) {
        // нельзя добавить повторный запрос
        if (requestRepository.findByEventAndAndRequester(request.getEvent(), request.getRequester()).isPresent()) {
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
        if (Objects.equals(eventRepository.getConfirmedRequestAmount(request.getEvent().getId()),
                request.getEvent().getParticipantLimit())) {
            throw new RequestValidationException("Cannot save same request – limit is already reached");
        }
        // если для события отключена пре-модерация запросов на участие,
        // то запрос должен автоматически перейти в состояние подтвержденного
        if (!request.getEvent().getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
        }
    }
}
