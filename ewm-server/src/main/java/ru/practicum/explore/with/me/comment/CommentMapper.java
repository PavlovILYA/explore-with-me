package ru.practicum.explore.with.me.comment;

import ru.practicum.explore.with.me.comment.dto.CommentCreateDto;
import ru.practicum.explore.with.me.comment.dto.CommentDto;
import ru.practicum.explore.with.me.comment.model.Comment;
import ru.practicum.explore.with.me.event.model.Event;
import ru.practicum.explore.with.me.user.model.User;

import java.time.LocalDateTime;

import static ru.practicum.explore.with.me.Constants.formatter;

public class CommentMapper {
    public static Comment toComment(CommentCreateDto commentCreateDto, boolean byAdmin, User commentator, Event event) {
        boolean byInitiator = !byAdmin && event.getInitiator().equals(commentator);
        return Comment.builder()
                .event(event)
                .text(commentCreateDto.getText())
                .commentator(byAdmin ? null : commentator)
                .publishedOn(LocalDateTime.now())
                .byAdmin(byAdmin)
                .byInitiator(byInitiator)
                .build();
    }

    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .eventId(comment.getEvent().getId())
                .text(comment.getText())
                .commentatorId(comment.getCommentator() == null ? null : comment.getCommentator().getId())
                .publishedOn(comment.getPublishedOn().format(formatter))
                .byAdmin(comment.getByAdmin())
                .byInitiator(comment.getByInitiator())
                .build();
    }
}
