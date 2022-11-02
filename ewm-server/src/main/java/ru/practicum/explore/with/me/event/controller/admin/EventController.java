package ru.practicum.explore.with.me.event.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.DateTimeEncoder;
import ru.practicum.explore.with.me.category.model.Category;
import ru.practicum.explore.with.me.category.service.CategoryService;
import ru.practicum.explore.with.me.event.dto.request.EventCreateDto;
import ru.practicum.explore.with.me.event.dto.response.EventFullDto;
import ru.practicum.explore.with.me.event.model.EventState;
import ru.practicum.explore.with.me.event.service.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RequestMapping("/admin/events")
@RestController("adminEventController")
public class EventController {
    private final EventService eventService;
    private final CategoryService categoryService;

    @Autowired
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
                                               @Positive
                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        LocalDateTime start = DateTimeEncoder.decodeAndParse(rangeStart);
        LocalDateTime end = DateTimeEncoder.decodeAndParse(rangeEnd);
        List<EventState> states = stringStates.stream()
                .map(EventState::valueOf)
                .collect(Collectors.toList());
        log.info("GET /admin/events users={} states={} categories={} rangeStart={} rangeEnd={} from={} size={}",
                userIds, states, categoryIds, start, end, from, size);
        return eventService.getEventsByAdmin(userIds, states, categoryIds, start, end, from, size);
    }

    @PutMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable("eventId") Long eventId,
                                           @RequestBody EventCreateDto eventCreateDto) {
        log.info("PUT /admin/events/{} eventCreateDto={}", eventId, eventCreateDto);
        Category category = eventCreateDto.getCategory() == null
                ? null
                : categoryService.getCategory(eventCreateDto.getCategory());
        return eventService.updateEventByAdmin(eventId, eventCreateDto, category);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEventByAdmin(@PathVariable("eventId") Long eventId) {
        log.info("PATCH /admin/events/{}/publish", eventId);
        return eventService.publishEventByAdmin(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEventByAdmin(@PathVariable("eventId") Long eventId) {
        log.info("PATCH /admin/events/{}/reject", eventId);
        return eventService.rejectEventByAdmin(eventId);
    }
}
