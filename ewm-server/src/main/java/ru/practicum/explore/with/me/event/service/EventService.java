package ru.practicum.explore.with.me.event.service;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore.with.me.category.model.Category;
import ru.practicum.explore.with.me.event.EventMapper;
import ru.practicum.explore.with.me.event.dto.request.EventCreateDto;
import ru.practicum.explore.with.me.event.dto.request.EventSort;
import ru.practicum.explore.with.me.event.dto.request.EventUpdateDto;
import ru.practicum.explore.with.me.event.dto.response.EventFullDto;
import ru.practicum.explore.with.me.event.exception.EventCancelException;
import ru.practicum.explore.with.me.event.exception.EventNotFoundException;
import ru.practicum.explore.with.me.event.exception.EventValidationException;
import ru.practicum.explore.with.me.event.model.DslPredicate;
import ru.practicum.explore.with.me.event.model.Event;
import ru.practicum.explore.with.me.event.model.EventState;
import ru.practicum.explore.with.me.event.repository.EventRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static ru.practicum.explore.with.me.event.model.QEvent.event;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public EventFullDto saveEventByUser(Event event) {
        Event savedEvent = eventRepository.save(event);
        log.info("Saved event: {}", savedEvent);
        return addConfirmedRequestsAndViews(savedEvent);
    }

    public EventFullDto updateEventByUser(Long userId, EventUpdateDto eventUpdateDto, Category category) {
        Event event = getEvent(eventUpdateDto.getEventId());
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new EventNotFoundException(eventUpdateDto.getEventId());
        }
        EventMapper.updateEvent(event, eventUpdateDto, category);
        Event updatedEvent = eventRepository.save(event);
        log.info("Updated event by initiator: {}", updatedEvent);
        return addConfirmedRequestsAndViews(updatedEvent);
    }

    public List<EventFullDto> getEventsByUser(Long userId, int from, int size) {
        Pageable pageRequest = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageRequest).getContent();
        log.info("All events of user {}: {}", userId, events);
        return events.stream()
                .map(this::addConfirmedRequestsAndViews)
                .collect(Collectors.toList());
    }

    public EventFullDto getEventByUser(Long userId, Long eventId) {
        Event event = eventRepository.findEventByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        log.info("Get event: {}", event);
        return addConfirmedRequestsAndViews(event);
    }

    // только событие в состоянии ожидания!
    public EventFullDto cancelEventByUser(Long userId, Long eventId) {
        Event event = getEvent(eventId);
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new EventNotFoundException(eventId);
        }
        if (!event.getState().equals(EventState.PENDING)) {
            throw new EventCancelException();
        }
        event.setState(EventState.CANCELED);
        Event canceledEvent = eventRepository.save(event);
        log.info("Event {} was canceled by initiator", userId);
        return addConfirmedRequestsAndViews(canceledEvent);
    }

    public List<EventFullDto> getEventsByAdmin(List<Long> userIds, List<EventState> states, List<Long> categoryIds,
                                        LocalDateTime start, LocalDateTime end, int from, int size) {
        Predicate predicate = DslPredicate.builder()
                .addListPredicate(userIds, event.initiator.id::in)
                .addListPredicate(states, event.state::in)
                .addListPredicate(categoryIds, event.category.id::in)
                .addPredicate(start, event.eventDate::goe)
                .addPredicate(end, event.eventDate::loe)
                .build();
        Pageable pageRequest = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findAll(predicate, pageRequest).getContent();
        log.info("Get events by admin: {}", events);
        return events.stream()
                .map(this::addConfirmedRequestsAndViews)
                .collect(Collectors.toList());
    }

    public EventFullDto updateEventByAdmin(Long eventId, EventCreateDto eventCreateDto, Category category) {
        Event event = getEvent(eventId);
        EventMapper.updateEventByAdmin(event, eventCreateDto, category);
        Event updatedEvent = eventRepository.save(event);
        log.info("Updated event by admin: {}", updatedEvent);
        return addConfirmedRequestsAndViews(updatedEvent);
    }

    public EventFullDto publishEventByAdmin(Long eventId) {
        Event event = getEvent(eventId);
        validateEventBeforePublishing(event);
        event.setPublishedOn(LocalDateTime.now());
        event.setState(EventState.PUBLISHED);
        Event publishedEvent = eventRepository.save(event);
        log.info("Published event by admin: {}", publishedEvent);
        return addConfirmedRequestsAndViews(publishedEvent);
    }

    public EventFullDto rejectEventByAdmin(Long eventId) {
        Event event = getEvent(eventId);
        validateEventBeforeRejecting(event);
        event.setState(EventState.CANCELED);
        Event rejectedEvent = eventRepository.save(event);
        log.info("Rejected (canceled) event by admin: {}", rejectedEvent);
        return addConfirmedRequestsAndViews(rejectedEvent);
    }

    public List<EventFullDto> getEventsAndConsiderStats(String text, Boolean paid,
                                                        LocalDateTime start, LocalDateTime end,
                                                        Boolean onlyAvailable, EventSort eventSort,
                                                        int from, int size,
                                                        String app, String uri, String ip) {
        Predicate predicate = DslPredicate.builder()
                .addPredicate(text, t -> event.description.containsIgnoreCase(t)
                        .or(event.annotation.containsIgnoreCase(t)))
                .addPredicate(paid, event.paid::eq)
                .addPredicate(start, event.eventDate::goe)
                .addPredicate(end, event.eventDate::loe)
                .build();
        Stream<Event> eventStream = StreamSupport.stream(
                eventRepository.findAll(predicate, Sort.by("eventDate")).spliterator(), false);
        if (onlyAvailable) {
            eventStream = getOnlyAvailable(eventStream);
        }
        Stream<EventFullDto> eventDtoStream = eventStream.map(this::addConfirmedRequestsAndViews);
        if (eventSort.equals(EventSort.VIEWS)) {
            eventDtoStream = eventDtoStream.sorted(Comparator.comparing(EventFullDto::getViews));
        }
//        List<EventFullDto> events = eventDtoStream.skip(from).limit(size).collect(Collectors.toList());
        return eventDtoStream.skip(from).limit(size).collect(Collectors.toList());
        // stats post
    }

    private Stream<Event> getOnlyAvailable(Stream<Event> eventStream) {
        return eventStream.filter(event ->
                eventRepository.getConfirmedRequestAmount(event.getId()) < event.getParticipantLimit());
    }

    public EventFullDto getEventAndConsiderStats(Long eventId, String app, String uri, String ip) {
        Event event = getEvent(eventId);
        validateBeforeGet(event);
        // stats post
        return addConfirmedRequestsAndViews(event);
    }

    public Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
    }

    public EventFullDto addConfirmedRequestsAndViews(Event event) {
        Integer confirmedRequests = eventRepository.getConfirmedRequestAmount(event.getId());
        // stats get
        return EventMapper.toFullDto(event, confirmedRequests);
    }

    private void validateEventBeforePublishing(Event event) {
        if (event.getEventDate().isBefore(
                LocalDateTime.now().plus(1, ChronoUnit.HOURS))) {
            throw new EventValidationException("It's too late for publishing this event– cannot publish");
        }
        if (!event.getState().equals(EventState.PENDING)) {
            throw new EventValidationException("The event is not pending – cannot publish");
        }
    }

    private void validateEventBeforeRejecting(Event event) {
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new EventValidationException("The event is published – cannot reject");
        }
    }

    private void validateBeforeGet(Event event) {
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new EventValidationException("The event is not published yet – cannot get");
        }
    }
}
