package ru.practicum.explore.with.me.event.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ru.practicum.explore.with.me.comment.dto.CommentDto;
import ru.practicum.explore.with.me.event.model.Location;

import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class EventFullDto extends EventShortDto {
    private String createdOn;
    private String description;
    private Location location;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private String state;
    private List<CommentDto> comments;
}
