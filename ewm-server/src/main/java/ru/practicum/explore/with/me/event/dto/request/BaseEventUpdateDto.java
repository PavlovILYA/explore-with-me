package ru.practicum.explore.with.me.event.dto.request;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Size;

@Data
@SuperBuilder(toBuilder = true)
public abstract class BaseEventUpdateDto {
    @Size(min = 20, max = 2000)
    private String annotation;
    private Integer category;
    @Size(min = 20, max = 7000)
    private String description;
    @FutureOrPresent
    private String eventDate;
    private Boolean paid;
    private Integer participantLimit;
    @Size(min = 3, max = 120)
    private String title;
}
