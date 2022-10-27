package ru.practicum.explore.with.me.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.with.me.category.model.Category;
import ru.practicum.explore.with.me.event.EventMapper;
import ru.practicum.explore.with.me.event.dto.request.EventUpdateDto;
import ru.practicum.explore.with.me.event.exception.EventCancelException;
import ru.practicum.explore.with.me.event.exception.EventNotFoundException;
import ru.practicum.explore.with.me.event.model.Event;
import ru.practicum.explore.with.me.event.model.EventState;
import ru.practicum.explore.with.me.event.repository.EventRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public Event saveEventByUser(Event event) {
        Event savedEvent = eventRepository.save(event);
        log.info("Saved event: {}", savedEvent);
        return savedEvent;
    }

    public Event updateEventByUser(Long userId, EventUpdateDto eventUpdateDto, Category category) {
        Event event = getEventByUser(userId, eventUpdateDto.getEventId());
        EventMapper.updateEvent(event, eventUpdateDto, category);
        Event updatedEvent = eventRepository.save(event);
        log.info("Updated event by initiator: {}", updatedEvent);
        return updatedEvent;
    }

    public List<Event> getEventsByUser(Long userId, int from, int size) {
        Pageable pageRequest = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageRequest).getContent();
        log.info("All events of user {}: {}", userId, events);
        return events;
    }

    public Event getEventByUser(Long userId, Long eventId) {
        Event event = eventRepository.findEventByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        log.info("Get event: {}", event);
        return event;
    }

    // только событие в состоянии ожидания!
    public Event cancelEventByUser(Long userId, Long eventId) {
        Event event = getEventByUser(userId, eventId);
        if (!event.getState().equals(EventState.PENDING)) {
            throw new EventCancelException();
        }
        event.setState(EventState.CANCELED);
        log.info("Event {} was canceled by initiator", userId);
        return eventRepository.save(event);
    }
}
