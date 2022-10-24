package ru.practicum.explore.with.me.hit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.with.me.hit.model.Hit;
import ru.practicum.explore.with.me.hit.model.HitStats;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {
    @Query("SELECT h.app AS app, h.uri AS uri, COUNT(h) AS count FROM Hit AS h" +
            " WHERE h.uri IN (:uris) AND h.timestamp >= :start AND h.timestamp <= :end" +
            " GROUP BY h.app, h.uri")
    List<HitStats> getHitStats(LocalDateTime start, LocalDateTime end, String uris);

    @Query(nativeQuery = true,
            value =
            "SELECT distinct_ip.app AS app, distinct_ip.uri AS uri, count(*) AS count FROM" +
            " (SELECT app, uri, ip FROM hits" +
            "  WHERE uri IN (:uris) AND date >= :start AND date <= :end" +
            "  GROUP BY app, uri, ip) AS distinct_ip" +
            " GROUP BY distinct_ip.app, distinct_ip.uri")
    List<HitStats> getHitsStatsWithDistinctIp(LocalDateTime start, LocalDateTime end, String uris);
}
