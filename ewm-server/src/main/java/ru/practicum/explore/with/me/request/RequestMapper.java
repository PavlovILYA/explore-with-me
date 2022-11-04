package ru.practicum.explore.with.me.request;

import ru.practicum.explore.with.me.event.model.Event;
import ru.practicum.explore.with.me.request.dto.RequestDto;
import ru.practicum.explore.with.me.request.model.ParticipationRequest;
import ru.practicum.explore.with.me.request.model.RequestStatus;
import ru.practicum.explore.with.me.user.model.User;

import java.time.LocalDateTime;

import static ru.practicum.explore.with.me.Constants.formatter;

public class RequestMapper {
    public static ParticipationRequest toRequest(User requester, Event event) {
        return ParticipationRequest.builder()
                .requester(requester)
                .event(event)
                .createdOn(LocalDateTime.now())
                .status(RequestStatus.PENDING)
                .build();
    }

    public static RequestDto toDto(ParticipationRequest request) {
        return RequestDto.builder()
                .id(request.getId())
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .created(request.getCreatedOn().format(formatter))
                .status(request.getStatus().name())
                .build();
    }
}
