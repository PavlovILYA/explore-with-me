package ru.practicum.explore.with.me.compilation.controller.open;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.compilation.CompilationMapper;
import ru.practicum.explore.with.me.compilation.dto.response.CompilationFullDto;
import ru.practicum.explore.with.me.compilation.model.Compilation;
import ru.practicum.explore.with.me.compilation.service.CompilationService;
import ru.practicum.explore.with.me.event.service.EventService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/compilations")
@RestController("PublicCompilationController")
public class CompilationController {
    private final CompilationService compilationService;
    private final EventService eventService;

    @GetMapping
    public List<CompilationFullDto> getCompilations(@RequestParam(value = "pinned", defaultValue = "false") boolean pinned,
                                                    @RequestParam(value = "from", defaultValue = "0") int from,
                                                    @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("GET /compilations pinned={} from={} size={}", pinned, from, size);
        return compilationService.getCompilations(pinned, from, size).stream()
                .map(compilation -> CompilationMapper.toFullDto(
                        compilation,
                        compilation.getEvents().stream()
                                .map(eventService::addConfirmedRequestsAndViews)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @GetMapping("/{compId}")
    public CompilationFullDto getCompilation(@PathVariable("compId") Long compilationId) {
        log.info("GET /compilations/{}", compilationId);
        Compilation compilation = compilationService.getCompilation(compilationId);
        return CompilationMapper.toFullDto(
                compilation,
                compilation.getEvents().stream()
                        .map(eventService::addConfirmedRequestsAndViews)
                        .collect(Collectors.toList()));
    }
}
