package ru.practicum.explore.with.me.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.with.me.event.model.Event;
import ru.practicum.explore.with.me.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "participation_requests")
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(table = "participation_requests", name = "event_id")
    private Event event;
    @ManyToOne
    @JoinColumn(table = "participation_requests", name = "requester_id")
    private User requester;
    private LocalDateTime createdOn;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}
