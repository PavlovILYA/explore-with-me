package ru.practicum.explore.with.me.event.controller.open;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.DateTImeEncoder;
import ru.practicum.explore.with.me.dto.HitDto;
import ru.practicum.explore.with.me.event.EventMapper;
import ru.practicum.explore.with.me.event.dto.request.EventSort;
import ru.practicum.explore.with.me.event.dto.response.EventFullDto;
import ru.practicum.explore.with.me.event.dto.response.EventShortDto;
import ru.practicum.explore.with.me.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.explore.with.me.Constants.formatter;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/events")
@RestController("PublicEventController")
public class EventController {
    private static final String APP = "ewm-main-service";
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam("text") String text,
                                         @RequestParam("paid") Boolean paid,
                                         @RequestParam("rangeStart") String rangeStart,
                                         @RequestParam("rangeEnd") String rangeEnd,
                                         @RequestParam(value = "onlyAvailable", defaultValue = "false")
                                             Boolean onlyAvailable,
                                         @RequestParam(value = "sort", defaultValue = "EVENT_DATE") String sort,
                                         @RequestParam(value = "from", defaultValue = "0") int from,
                                         @RequestParam(value = "size", defaultValue = "10") int size,
                                         HttpServletRequest request) {
        HitDto hitDto = makeHitDto(request.getRequestURI(), request.getRemoteAddr());
        log.info("HIT: {}", hitDto);
        LocalDateTime start = DateTImeEncoder.decodeAndParse(rangeStart);
        LocalDateTime end = DateTImeEncoder.decodeAndParse(rangeEnd);
        EventSort eventSort = EventSort.valueOf(sort);
        log.info("GET /events text={} paid={} start={} end={} onlyAvailable={} sort={} from={} size={}",
                text, paid, start, end, onlyAvailable, eventSort, from, size);
        return eventService.getEventsAndConsiderStats(text, paid, start, end,onlyAvailable, eventSort, from, size,
                        hitDto)
                .stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventAndConsiderStats(@PathVariable("eventId") Long eventId,
                                                 HttpServletRequest request) {
        HitDto hitDto = makeHitDto(request.getRequestURI(), request.getRemoteAddr());
        log.info("HIT: {}", hitDto);
        log.info("GET /events/{}", eventId);
        return eventService.getEventAndConsiderStats(eventId, hitDto);
    }

    private HitDto makeHitDto(String uri, String ip) {
        return HitDto.builder()
                .app(APP)
                .uri(uri)
                .ip(ip)
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
    }
}
