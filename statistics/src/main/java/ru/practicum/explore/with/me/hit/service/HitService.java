package ru.practicum.explore.with.me.hit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore.with.me.hit.model.Hit;
import ru.practicum.explore.with.me.hit.model.HitStats;
import ru.practicum.explore.with.me.hit.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HitService {
    private final HitRepository hitRepository;

    public Hit saveHit(Hit hit) {
        log.info("Saving {}", hit);
        return hitRepository.save(hit);
    }

    public List<HitStats> getStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        log.info("Get statistics: start={} end={} uris={} unique={}", start, end, uris, unique);
        if (unique) {
            return hitRepository.getHitsStatsWithDistinctIp(start, end, uris);
        } else {
            return hitRepository.getHitStats(start, end, uris);
        }
    }
}
