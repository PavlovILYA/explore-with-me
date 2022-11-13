package ru.practicum.explore.with.me.event.controller.open;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.DateTimeEncoder;
import ru.practicum.explore.with.me.dto.HitDto;
import ru.practicum.explore.with.me.event.EventMapper;
import ru.practicum.explore.with.me.event.dto.request.EventSort;
import ru.practicum.explore.with.me.event.dto.response.EventFullDto;
import ru.practicum.explore.with.me.event.dto.response.EventShortDto;
import ru.practicum.explore.with.me.event.exception.EventValidationException;
import ru.practicum.explore.with.me.event.model.Location;
import ru.practicum.explore.with.me.event.model.SearchArea;
import ru.practicum.explore.with.me.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.explore.with.me.Constants.formatter;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/events")
@RestController("publicEventController")
public class EventController {
    private static final String APP = "ewm-main-service";
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getEvents(@PositiveOrZero
                                         @RequestParam(value = "lat", required = false) Double lat,
                                         @PositiveOrZero
                                         @RequestParam(value = "lon", required = false) Double lon,
                                         @Positive
                                         @RequestParam(value = "radius", required = false) Double radius,
                                         @RequestParam(value = "text", required = false) String text,
                                         @RequestParam(value = "paid", required = false) Boolean paid,
                                         @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                         @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                         @RequestParam(value = "onlyAvailable", defaultValue = "false")
                                             Boolean onlyAvailable,
                                         @RequestParam(value = "sort", defaultValue = "EVENT_DATE") String sort,
                                         @PositiveOrZero
                                         @RequestParam(value = "from", defaultValue = "0") int from,
                                         @Positive
                                         @RequestParam(value = "size", defaultValue = "10") int size,
                                         HttpServletRequest request) {
        SearchArea searchArea = parseSearchArea(lat, lon, radius);
        LocalDateTime start = DateTimeEncoder.decodeAndParse(rangeStart);
        LocalDateTime end = DateTimeEncoder.decodeAndParse(rangeEnd);
        EventSort eventSort = EventSort.valueOf(sort);
        log.info("GET /events text={} paid={} start={} end={} onlyAvailable={} sort={} from={} size={} searchArea={}",
                text, paid, start, end, onlyAvailable, eventSort, from, size, searchArea);
        HitDto hitDto = makeHitDto(request.getRequestURI(), request.getRemoteAddr());
        log.info("HIT: {}", hitDto);
        return eventService.getEventsAndConsiderStats(text, paid, start, end,onlyAvailable, eventSort, searchArea,
                        from, size,
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

    private SearchArea parseSearchArea(Double lat, Double lon, Double radius) {
        if (radius == null && lat == null && lon == null) {
            return null;
        }
        if (radius != null && lat != null && lon != null) {
            return new SearchArea(new Location(lat, lon), radius);
        } else {
            throw new EventValidationException("Wrong search area params: lat=" + lat +
                    " lon=" + lon + " radius=" + radius);
        }
    }
}
