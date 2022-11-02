package ru.practicum.explore.with.me.hit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.DateTimeEncoder;
import ru.practicum.explore.with.me.hit.HitMapper;
import ru.practicum.explore.with.me.hit.dto.HitDto;
import ru.practicum.explore.with.me.hit.model.Hit;
import ru.practicum.explore.with.me.hit.model.HitStats;
import ru.practicum.explore.with.me.hit.service.HitService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HitController {
    private final HitService hitService;

    @PostMapping("/hit")
    public HitDto saveHit(@RequestBody @Valid HitDto hitDto) {
        log.info("POST {} hitDto={}", "/hit", hitDto);
        Hit hit = HitMapper.toHit(hitDto);
        return HitMapper.toHitDto(hitService.saveHit(hit));
    }

    @GetMapping("/stats")
    public List<HitStats> getStats(@RequestParam("start") String encodedStartString,
                                   @RequestParam("end") String encodedEndString,
                                   @RequestParam("uris") String[] uris,
                                   @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        LocalDateTime start = DateTimeEncoder.decodeAndParse(encodedStartString);
        LocalDateTime end = DateTimeEncoder.decodeAndParse(encodedEndString);
        log.info("GET {} start={} end={} uris={} unique={}", "/stats", start, end, uris, unique);
        return hitService.getStats(start, end, uris, unique);
    }
}
