package ru.practicum.explore.with.me.event.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class EventUpdateDto extends BaseEventUpdateDto {
    private Long eventId;
}
