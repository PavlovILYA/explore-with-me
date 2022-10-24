package ru.practicum.explore.with.me.hit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.with.me.hit.model.Hit;
import ru.practicum.explore.with.me.hit.model.HitStats;
import ru.practicum.explore.with.me.hit.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HitService {
    private final HitRepository hitRepository;

    public Hit saveHit(Hit hit) {
        return hitRepository.save(hit);
    }

    public List<HitStats> getStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        String urisAsString = String.join(", ", uris);
        if (unique) {
            return hitRepository.getHitsStatsWithDistinctIp(start, end, urisAsString);
        } else {
            return hitRepository.getHitStats(start, end, urisAsString);
        }
    }
}
