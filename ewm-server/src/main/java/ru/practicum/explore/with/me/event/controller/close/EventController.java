package ru.practicum.explore.with.me.event.controller.close;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.category.model.Category;
import ru.practicum.explore.with.me.category.service.CategoryService;
import ru.practicum.explore.with.me.event.EventMapper;
import ru.practicum.explore.with.me.event.dto.request.EventCreateDto;
import ru.practicum.explore.with.me.event.dto.request.EventUpdateDto;
import ru.practicum.explore.with.me.event.dto.response.EventFullDto;
import ru.practicum.explore.with.me.event.dto.response.EventShortDto;
import ru.practicum.explore.with.me.event.model.Event;
import ru.practicum.explore.with.me.event.service.EventService;
import ru.practicum.explore.with.me.user.model.User;
import ru.practicum.explore.with.me.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RequestMapping("/users")
@RestController("PrivateEventController")
public class EventController {
    private final EventService eventService;
    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
    public EventController(EventService eventService, UserService userService, CategoryService categoryService) {
        this.eventService = eventService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @PostMapping("/{userId}/events")
    public EventFullDto saveEventByUser(@Valid @RequestBody EventCreateDto eventCreateDto,
                                        @PathVariable Long userId) {
        log.info("POST /users/{}/events eventCreateDto={}", userId, eventCreateDto);
        User user = userService.getUser(userId);
        Category category = categoryService.getCategory(eventCreateDto.getCategory());
        Event event = EventMapper.toEvent(eventCreateDto, category, user);
        return eventService.saveEventByUser(event);
    }

    @PatchMapping("/{userId}/events")
    public EventFullDto updateEventByUser(@Valid @RequestBody EventUpdateDto eventUpdateDto,
                                          @PathVariable Long userId) {
        log.info("PATCH /users/{}/events eventUpdateDto={}", userId, eventUpdateDto);
        Category category = eventUpdateDto.getCategory() == null
                ? null
                : categoryService.getCategory(eventUpdateDto.getCategory());
        return eventService.updateEventByUser(userId, eventUpdateDto, category);
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEventsByUser(@PathVariable Long userId,
                                                         @PositiveOrZero
                                                         @RequestParam(value = "from", defaultValue = "0") int from,
                                                         @Positive
                                                         @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("GET /users/{}/events from={} size={}", userId, from, size);
        return eventService.getEventsByUser(userId, from, size).stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventByUser(@PathVariable("userId") Long userId,
                                       @PathVariable("eventId") Long eventId) {
        log.info("GET /users/{}/events/{}", userId, eventId);
        return eventService.getEventByUser(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}") // только событие в состоянии ожидания!
    public EventFullDto cancelEventByUser(@PathVariable("userId") Long userId,
                                          @PathVariable("eventId") Long eventId) {
        log.info("PATCH /users/{}/events/{} – cancel event", userId, eventId);
        return eventService.cancelEventByUser(userId, eventId);
    }
}
