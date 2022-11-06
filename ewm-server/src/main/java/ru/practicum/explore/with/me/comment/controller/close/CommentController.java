package ru.practicum.explore.with.me.comment.controller.close;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.comment.CommentMapper;
import ru.practicum.explore.with.me.comment.dto.CommentCreateDto;
import ru.practicum.explore.with.me.comment.dto.CommentDto;
import ru.practicum.explore.with.me.comment.model.Comment;
import ru.practicum.explore.with.me.comment.service.CommentService;
import ru.practicum.explore.with.me.event.model.Event;
import ru.practicum.explore.with.me.event.service.EventService;
import ru.practicum.explore.with.me.user.model.User;
import ru.practicum.explore.with.me.user.service.UserService;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@RestController("privateCommentController")
public class CommentController {
    private final CommentService commentService;
    private final EventService eventService;
    private final UserService userService;

    @PostMapping("/{userId}/events/{eventId}/comments")
    public CommentDto saveComment(@PathVariable("userId") Long userId,
                                  @PathVariable("eventId") Long eventId,
                                  @Valid @RequestBody CommentCreateDto commentCreateDto) {
        log.info("POST /users/{}/events/{}/comments commentCreateDto: {}", userId, eventId, commentCreateDto);
        Event event = eventService.getEvent(eventId);
        User commentator = userService.getUser(userId);
        boolean byAdmin = false;
        Comment comment = CommentMapper.toComment(commentCreateDto, byAdmin, commentator, event);
        return CommentMapper.toDto(commentService.saveComment(comment));
    }

    @DeleteMapping("/{userId}/events/{eventId}/comments/{commentId}")
    public void deleteComment(@PathVariable("userId") Long userId,
                              @PathVariable("eventId") Long eventId,
                              @PathVariable("commentId") Long commentId) {
        log.info("DELETE /users/{}/events/{}/comments/{}", userId, eventId, commentId);
        commentService.deleteCommentByCommentator(userId, eventId, commentId);
    }
}
