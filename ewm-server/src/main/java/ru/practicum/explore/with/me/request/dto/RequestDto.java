package ru.practicum.explore.with.me.request.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class RequestDto {
    private Long id;
    private Long event;
    private Long requester;
    private String created;
    private String status;
}
