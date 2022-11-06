package ru.practicum.explore.with.me.comment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class CommentDto {
    private Long id;
    private Long eventId;
    private String text;
    private Long commentatorId;
    private String publishedOn;
    private Boolean byAdmin;
    private Boolean byInitiator;
}
