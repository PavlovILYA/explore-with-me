package ru.practicum.explore.with.me.event.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

//import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class BaseEventUpdateDto {
    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000)
    private String description;
    private String eventDate;
    private Boolean paid;
    private Integer participantLimit;
    @Size(min = 3, max = 120)
    private String title;
}
