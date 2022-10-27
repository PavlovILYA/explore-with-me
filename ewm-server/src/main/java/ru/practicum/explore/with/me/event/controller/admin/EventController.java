package ru.practicum.explore.with.me.event.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.DateTImeEncoder;
import ru.practicum.explore.with.me.category.model.Category;
import ru.practicum.explore.with.me.category.service.CategoryService;
import ru.practicum.explore.with.me.event.EventMapper;
import ru.practicum.explore.with.me.event.dto.request.EventCreateDto;
import ru.practicum.explore.with.me.event.dto.response.EventFullDto;
import ru.practicum.explore.with.me.event.model.EventState;
import ru.practicum.explore.with.me.event.service.admin.EventService;

import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RequestMapping("/admin/events")
@RestController("AdminEventController")
public class EventController {
    @Qualifier("AdminEventService")
    private final EventService eventService;
    private final CategoryService categoryService;

    public EventController(EventService eventService, CategoryService categoryService) {
        this.eventService = eventService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<EventFullDto> getEventsByAdmin(@RequestParam(value = "users", defaultValue = "null") List<Long> userIds,
                                               @RequestParam(value = "states", defaultValue = "null") List<String> stringStates,
                                               @RequestParam(value = "categories", defaultValue = "null") List<Long> categoryIds,
                                               @RequestParam(value = "rangeStart", defaultValue = "null") String rangeStart,
                                               @RequestParam(value = "rangeEnd", defaultValue = "null") String rangeEnd,
                                               @PositiveOrZero
                                               @RequestParam(value = "from", defaultValue = "0") int from,
                                               @PositiveOrZero
                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        LocalDateTime start = DateTImeEncoder.decodeAndParse(rangeStart);
        LocalDateTime end = DateTImeEncoder.decodeAndParse(rangeEnd);
        List<EventState> states = stringStates.stream()
                .map(EventState::valueOf)
                .collect(Collectors.toList());
        log.info("GET /admin/events users={} states={} categories={} rangeStart={} rangeEnd={} from={} size={}",
                userIds, states, categoryIds, start, end, from, size);
        return eventService.getEventsByAdmin(userIds, states, categoryIds, start, end, from, size).stream()
                .map(EventMapper::toFullDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable("eventId") Long eventId,
                                           @RequestBody EventCreateDto eventCreateDto) {
        log.info("PUT /admin/events/{} eventCreateDto={}", eventId, eventCreateDto);
        Category category = eventCreateDto.getCategory() == null
                ? null
                : categoryService.getCategory(eventCreateDto.getCategory());
        return EventMapper.toFullDto(
                eventService.updateEventByAdmin(eventId, eventCreateDto, category));
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEventByAdmin(@PathVariable("eventId") Long eventId) {
        log.info("PATCH /admin/events/{}/publish", eventId);
        return EventMapper.toFullDto(
                eventService.publishEventByAdmin(eventId));
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEventByAdmin(@PathVariable("eventId") Long eventId) {
        log.info("PATCH /admin/events/{}/reject", eventId);
        return EventMapper.toFullDto(
                eventService.rejectEventByAdmin(eventId));
    }
}
