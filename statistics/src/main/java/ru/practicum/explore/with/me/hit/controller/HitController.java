package ru.practicum.explore.with.me.hit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore.with.me.DateTImeEncoder;
import ru.practicum.explore.with.me.hit.HitMapper;
import ru.practicum.explore.with.me.hit.dto.HitDto;
import ru.practicum.explore.with.me.hit.model.Hit;
import ru.practicum.explore.with.me.hit.model.HitStats;
import ru.practicum.explore.with.me.hit.service.HitService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HitController {
    private final HitService hitService;

    @PostMapping("/hit")
    public HitDto saveHit(@Valid HitDto hitDto) {
        Hit hit = HitMapper.toHit(hitDto);
        return HitMapper.toHitDto(hitService.saveHit(hit));
    }

    @GetMapping("/stats")
    public List<HitStats> getStats(@RequestParam("start") String encodedStartString,
                                   @RequestParam("end") String encodedEndString,
                                   @RequestParam("uris[]") String[] uris,
                                   @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        LocalDateTime start = DateTImeEncoder.decodeAndParse(encodedStartString);
        LocalDateTime end = DateTImeEncoder.decodeAndParse(encodedEndString);
        return hitService.getStats(start, end, uris, unique);
    }
}
