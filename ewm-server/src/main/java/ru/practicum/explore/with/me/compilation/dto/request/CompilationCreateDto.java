package ru.practicum.explore.with.me.compilation.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class CompilationCreateDto {
    @NotBlank
    private String title;
    private List<Long> events;
    private Boolean pinned;
}
