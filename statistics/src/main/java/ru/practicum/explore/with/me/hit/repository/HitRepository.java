package ru.practicum.explore.with.me.hit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.with.me.hit.model.HitStats;
import ru.practicum.explore.with.me.hit.model.Hit;


import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {
    @Query("SELECT h.app AS app, h.uri AS uri, COUNT(h) AS count FROM Hit AS h" +
            " WHERE h.uri IN (:uris) AND h.timestamp >= :start AND h.timestamp <= :end" +
            " GROUP BY h.app, h.uri")
    List<HitStats> getHitStats(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("SELECT h.app AS app, h.uri AS uri, COUNT(DISTINCT h.ip) AS count FROM Hit AS h" +
            " WHERE h.uri IN (:uris) AND h.timestamp >= :start AND h.timestamp <= :end" +
            " GROUP BY h.app, h.uri")
    List<HitStats> getHitsStatsWithDistinctIp(LocalDateTime start, LocalDateTime end, String[] uris);
}
