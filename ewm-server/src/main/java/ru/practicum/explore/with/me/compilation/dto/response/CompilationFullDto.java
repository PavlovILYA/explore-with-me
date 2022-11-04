package ru.practicum.explore.with.me.compilation.dto.response;

import lombok.Builder;
import lombok.Data;
import ru.practicum.explore.with.me.event.dto.response.EventShortDto;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class CompilationFullDto {
    private Long id;
    private String title;
    private List<? extends EventShortDto> events;
    private Boolean pinned;
}
