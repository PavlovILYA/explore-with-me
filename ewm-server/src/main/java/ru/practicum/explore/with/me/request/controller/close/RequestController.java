package ru.practicum.explore.with.me.request.controller.close;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.event.model.Event;
import ru.practicum.explore.with.me.event.service.EventService;
import ru.practicum.explore.with.me.request.RequestMapper;
import ru.practicum.explore.with.me.request.dto.RequestDto;
import ru.practicum.explore.with.me.request.model.ParticipationRequest;
import ru.practicum.explore.with.me.request.service.RequestService;
import ru.practicum.explore.with.me.user.model.User;
import ru.practicum.explore.with.me.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;
    private final EventService eventService;
    private final UserService userService;

    @PostMapping("/{userId}/requests")
    public RequestDto saveRequest(@PathVariable("userId") Long userId,
                                  @RequestParam("eventId") Long eventId) {
        log.info("POST /users/{}/requests eventId={}", userId, eventId);
        User requester = userService.getUser(userId);
        Event event = eventService.getEvent(eventId);
        ParticipationRequest request = RequestMapper.toRequest(requester, event);
        return RequestMapper.toDto(
                requestService.saveRequest(request));
    }

    @GetMapping("/{userId}/requests")
    public List<RequestDto> getMyRequests(@PathVariable("userId") Long userId) {
        log.info("GET /users/{}/requests", userId);
        return requestService.getMyRequests(userId).stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable("userId") Long userId,
                                    @PathVariable("requestId") Long requestId) {
        log.info("PATCH /users/{}/requests/{}/cancel", userId, requestId);
        return RequestMapper.toDto(
                requestService.cancelRequest(userId, requestId));
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<RequestDto> getRequestsToMyEvent(@PathVariable("userId") Long userId,
                                                 @PathVariable("eventId") Long requestId) {
        return null;
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public RequestDto confirmRequestToMyEvent(@PathVariable("userId") Long userId,
                                              @PathVariable("eventId") Long eventId,
                                              @PathVariable("reqId") Long requestId) {
        return null;
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public RequestDto rejectRequestToMyEvent(@PathVariable("userId") Long userId,
                                             @PathVariable("eventId") Long eventId,
                                             @PathVariable("reqId") Long requestId) {
        return null;
    }
}
