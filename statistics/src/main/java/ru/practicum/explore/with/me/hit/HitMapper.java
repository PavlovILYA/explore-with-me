package ru.practicum.explore.with.me.hit;

import ru.practicum.explore.with.me.hit.dto.HitDto;
import ru.practicum.explore.with.me.hit.model.Hit;

import java.time.LocalDateTime;

import static ru.practicum.explore.with.me.Constants.formatter;

public class HitMapper {
    public static Hit toHit(HitDto hitDto) {
        return Hit.builder()
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .timestamp(LocalDateTime.parse(hitDto.getTimestamp(), formatter))
                .build();
    }

    public static HitDto toHitDto(Hit hit) {
        return HitDto.builder()
                .id(hit.getId())
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp().format(formatter))
                .build();
    }
}
