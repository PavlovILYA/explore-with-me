package ru.practicum.explore.with.me.event;

import ru.practicum.explore.with.me.category.CategoryMapper;
import ru.practicum.explore.with.me.category.model.Category;
import ru.practicum.explore.with.me.event.dto.request.BaseEventUpdateDto;
import ru.practicum.explore.with.me.event.dto.request.EventCreateDto;
import ru.practicum.explore.with.me.event.dto.response.EventFullDto;
import ru.practicum.explore.with.me.event.model.Event;
import ru.practicum.explore.with.me.event.model.EventState;
import ru.practicum.explore.with.me.user.UserMapper;
import ru.practicum.explore.with.me.user.model.User;

import java.time.LocalDateTime;

import static ru.practicum.explore.with.me.Constants.formatter;

public class EventMapper {
    public static Event toEvent(EventCreateDto eventCreateDto, Category category, User initiator) {
        eventCreateDto.setDefaultsForNew();
        return Event.builder()
                .annotation(eventCreateDto.getAnnotation())
                .category(category)
                .description(eventCreateDto.getDescription())
                .eventDate(LocalDateTime.parse(eventCreateDto.getEventDate(), formatter))
                .location(eventCreateDto.getLocation())
                .paid(eventCreateDto.getPaid())
                .participantLimit(eventCreateDto.getParticipantLimit())
                .requestModeration(eventCreateDto.getRequestModeration())
                .title(eventCreateDto.getTitle())
                .createdOn(LocalDateTime.now())
                .initiator(initiator)
                .state(EventState.PENDING)
                .build();
    }

    public static void updateEventByAdmin(Event event, EventCreateDto eventDto, Category category) {
        updateEvent(event, eventDto, category);
        if (eventDto.getLocation() != null) {
            event.setLocation(eventDto.getLocation());
        }
        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }
    }

    public static void updateEvent(Event event, BaseEventUpdateDto eventDto, Category category) {
        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (category != null) {
            event.setCategory(category);
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(eventDto.getEventDate(), formatter));
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
    }

    public static EventFullDto toFullDto(Event event, Integer confirmedRequests) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .createdOn(event.getCreatedOn().format(formatter))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(formatter))
                .initiator(UserMapper.userShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn() == null ? null : event.getPublishedOn().format(formatter))
                .requestModeration(event.getRequestModeration())
                .state(event.getState().name())
                .title(event.getTitle())
//                .views() // ?
                .build();
    }
}
