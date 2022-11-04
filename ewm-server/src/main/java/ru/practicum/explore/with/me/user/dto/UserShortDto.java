package ru.practicum.explore.with.me.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class UserShortDto {
    private Long id;
    private String name;
}
