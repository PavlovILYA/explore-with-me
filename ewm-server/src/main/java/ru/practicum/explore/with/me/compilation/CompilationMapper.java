package ru.practicum.explore.with.me.compilation;

import ru.practicum.explore.with.me.compilation.dto.request.CompilationCreateDto;
import ru.practicum.explore.with.me.compilation.dto.response.CompilationFullDto;
import ru.practicum.explore.with.me.compilation.model.Compilation;
import ru.practicum.explore.with.me.event.EventMapper;
import ru.practicum.explore.with.me.event.dto.response.EventFullDto;
import ru.practicum.explore.with.me.event.model.Event;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation toCompilation(CompilationCreateDto compilationCreateDto, List<Event> events) {
        return Compilation.builder()
                .title(compilationCreateDto.getTitle())
                .events(events)
                .pinned(compilationCreateDto.getPinned())
                .build();
    }

    public static CompilationFullDto toFullDto(Compilation compilation, List<EventFullDto> events) {
        return CompilationFullDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .events(events.stream()
                        .map(EventMapper::toShortDto)
                        .collect(Collectors.toList()))
                .pinned(compilation.getPinned())
                .build();
    }
}
