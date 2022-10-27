package ru.practicum.explore.with.me.event.service.admin;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.with.me.category.model.Category;
import ru.practicum.explore.with.me.event.EventMapper;
import ru.practicum.explore.with.me.event.dto.request.EventCreateDto;
import ru.practicum.explore.with.me.event.exception.EventNotFoundException;
import ru.practicum.explore.with.me.event.exception.EventPublishException;
import ru.practicum.explore.with.me.event.model.DslPredicate;
import ru.practicum.explore.with.me.event.model.Event;
import ru.practicum.explore.with.me.event.model.EventState;
import ru.practicum.explore.with.me.event.repository.EventRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.practicum.explore.with.me.event.model.QEvent.event;

@Slf4j
@RequiredArgsConstructor
@Service("AdminEventService")
public class EventService {
    private final EventRepository eventRepository;

    public List<Event> getEventsByAdmin(List<Long> userIds, List<EventState> states, List<Long> categoryIds,
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
        return events;
    }

    public Event updateEventByAdmin(Long eventId, EventCreateDto eventCreateDto, Category category) {
        Event event = getEvent(eventId);
        EventMapper.updateEventByAdmin(event, eventCreateDto, category);
        Event updatedEvent = eventRepository.save(event);
        log.info("Updated event by admin: {}", updatedEvent);
        return updatedEvent;
    }

    public Event publishEventByAdmin(Long eventId) {
        Event event = getEvent(eventId);
        validateEventBeforePublishing(event);
        event.setPublishedOn(LocalDateTime.now());
        event.setState(EventState.PUBLISHED);
        Event publishedEvent = eventRepository.save(event);
        log.info("Published event by admin: {}", publishedEvent);
        return publishedEvent;
    }

    public Event rejectEventByAdmin(Long eventId) {
        Event event = getEvent(eventId);
        validateEventBeforeRejecting(event);
        event.setState(EventState.CANCELED);
        Event rejectedEvent = eventRepository.save(event);
        log.info("Rejected (canceled) event by admin: {}", rejectedEvent);
        return rejectedEvent;
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
    }

    private void validateEventBeforePublishing(Event event) {
        if (event.getEventDate().isBefore(
                LocalDateTime.now().plus(1, ChronoUnit.HOURS))) {
            throw new EventPublishException("It's too late for publishing this event– cannot publish");
        }
        if (!event.getState().equals(EventState.PENDING)) {
            throw new EventPublishException("The event is not pending – cannot publish");
        }
    }

    private void validateEventBeforeRejecting(Event event) {
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new EventPublishException("The event is published – cannot reject");
        }
    }
}
