package ru.practicum.explore.with.me.comment.controller.admin;

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

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/events")
@RestController("adminCommentController")
public class CommentController {
    private final CommentService commentService;
    private final EventService eventService;

    @PostMapping("/{eventId}/comments")
    public CommentDto saveComment(@PathVariable("eventId") Long eventId,
                                  @Valid @RequestBody CommentCreateDto commentCreateDto) {
        log.info("POST /admin/events/{}/comments commentCreateDto: {}", eventId, commentCreateDto);
        Event event = eventService.getEvent(eventId);
        boolean byAdmin = true;
        Comment comment = CommentMapper.toComment(commentCreateDto, byAdmin, null, event);
        return CommentMapper.toDto(commentService.saveComment(comment));
    }

    @DeleteMapping("/{eventId}/comments/{commentId}")
    public void deleteComment(@PathVariable("eventId") Long eventId,
                              @PathVariable("commentId")Long commentId) {
        log.info("DELETE /admin/events/{}/comments/{}", eventId, commentId);
        commentService.deleteCommentByAdmin(eventId, commentId);
    }
}
