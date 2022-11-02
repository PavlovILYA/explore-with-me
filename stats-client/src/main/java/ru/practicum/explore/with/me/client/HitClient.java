package ru.practicum.explore.with.me.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explore.with.me.DateTimeEncoder;
import ru.practicum.explore.with.me.dto.HitDto;
import ru.practicum.explore.with.me.dto.HitStats;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class HitClient {
    private final RestTemplate restTemplate;

    @Autowired
    public HitClient(@Value("${statistics.server.url}") String statsServerUrl,
                     RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(statsServerUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public Boolean saveHit(HitDto hitDto) {
        log.info("Saving hitDto: {}", hitDto);
        HttpEntity<HitDto> requestEntity = new HttpEntity<>(hitDto, defaultHeaders());
        return checkResponse(restTemplate.postForEntity("/hit", requestEntity, HitDto.class));
    }

    public List<HitStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        Map<String, String> parameters = gatherParameters(start, end, uris, unique);
        log.info("Get stats: parameters={}", parameters);
        ResponseEntity<HitStats[]> response = restTemplate.getForEntity(
                "/stats?start={start}&end={end}&uris={uris}&unique={unique}", HitStats[].class, parameters);
        return checkResponse(response) ? Arrays.asList(Objects.requireNonNull(response.getBody())) : null;
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private Boolean checkResponse(ResponseEntity<?> response) {
        return response.getStatusCode().is2xxSuccessful();
    }

    private Map<String, String> gatherParameters(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        String urisString = String.join(",", uris);
        return Map.of(
                "start", DateTimeEncoder.encode(start),
                "end", DateTimeEncoder.encode(end),
                "unique", unique.toString(),
                "uris", urisString
        );
    }
}
