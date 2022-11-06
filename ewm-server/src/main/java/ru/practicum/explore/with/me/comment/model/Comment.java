package ru.practicum.explore.with.me.comment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.explore.with.me.event.model.Event;
import ru.practicum.explore.with.me.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "comments")
@Builder(toBuilder = true)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(table = "comments", name = "event_id")
    private Event event;
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(table = "comments", name = "commentator_id")
    private User commentator;
    private LocalDateTime publishedOn;
    private Boolean byAdmin;
    private Boolean byInitiator;
}
