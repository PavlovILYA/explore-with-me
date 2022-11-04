package ru.practicum.explore.with.me.compilation.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.compilation.CompilationMapper;
import ru.practicum.explore.with.me.compilation.dto.request.CompilationCreateDto;
import ru.practicum.explore.with.me.compilation.dto.response.CompilationFullDto;
import ru.practicum.explore.with.me.compilation.model.Compilation;
import ru.practicum.explore.with.me.compilation.service.CompilationService;
import ru.practicum.explore.with.me.event.model.Event;
import ru.practicum.explore.with.me.event.service.EventService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
@RestController("adminCompilationController")
public class CompilationController {
    private final CompilationService compilationService;
    private final EventService eventService;

    @PostMapping
    public CompilationFullDto saveCompilation(@Valid @RequestBody CompilationCreateDto compilationCreateDto) {
        log.info("POST /admin/compilations compilationCreateDto={}", compilationCreateDto);
        List<Event> events = compilationCreateDto.getEvents().stream()
                .map(eventService::getEvent)
                .collect(Collectors.toList());
        Compilation compilation = CompilationMapper.toCompilation(compilationCreateDto, events);
        return CompilationMapper.toFullDto(
                compilationService.saveCompilation(compilation),
                events.stream()
                        .map(eventService::addConfirmedRequestsAndViews)
                        .collect(Collectors.toList()));
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable("compId") Long compilationId) {
        log.info("DELETE /admin/compilations/{}", compilationId);
        compilationService.deleteCompilation(compilationId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable("compId") Long compilationId,
                                      @PathVariable("eventId") Long eventId) {
        log.info("PATCH /admin/compilations/{}/events/{}", compilationId, eventId);
        Event event = eventService.getEvent(eventId);
        compilationService.addEventToCompilation(compilationId, event);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable("compId") Long compilationId,
                                           @PathVariable("eventId") Long eventId) {
        log.info("DELETE /admin/compilations/{}/events/{}", compilationId, eventId);
        Event event = eventService.getEvent(eventId);
        compilationService.deleteEventFromCompilation(compilationId, event);
    }

    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable("compId") Long compilationId) {
        log.info("PATCH /admin/compilations/{}/pin", compilationId);
        compilationService.pinCompilation(compilationId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpinCompilation(@PathVariable("compId") Long compilationId) {
        log.info("DELETE /admin/compilations/{}/pin", compilationId);
        compilationService.unpinCompilation(compilationId);
    }
}
