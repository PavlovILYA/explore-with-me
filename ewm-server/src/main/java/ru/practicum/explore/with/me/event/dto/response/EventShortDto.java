package ru.practicum.explore.with.me.event.dto.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.explore.with.me.category.dto.CategoryDto;
import ru.practicum.explore.with.me.user.dto.UserShortDto;

@Data
@SuperBuilder(toBuilder = true)
public class EventShortDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String eventDate;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}
