package ru.practicum.explore.with.me.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore.with.me.comment.exception.CommentNotFoundException;
import ru.practicum.explore.with.me.comment.exception.CommentValidationException;
import ru.practicum.explore.with.me.comment.model.Comment;
import ru.practicum.explore.with.me.comment.repository.CommentRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Comment saveComment(Comment comment) {
        Comment savedComment = commentRepository.save(comment);
        log.info("Saved comment: {}", savedComment);
        return savedComment;
    }

    public void deleteCommentByCommentator(Long userId, Long eventId, Long commentId) {
        Comment comment = getComment(commentId);
        validateComment(comment, userId, eventId);
        commentRepository.delete(comment);
        log.info("Comment {} was deleted by commentator", commentId);
    }

    public void deleteCommentByAdmin(Long eventId, Long commentId) {
        Comment comment = getComment(commentId);
        validateEventComment(comment, eventId);
        commentRepository.delete(comment);
        log.info("Comment {} was deleted by admin", commentId);
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
    }

    private void validateComment(Comment comment, Long userId, Long eventId) {
        if (!comment.getCommentator().getId().equals(userId)) {
            throw new CommentValidationException("Cannot delete comment: no access");
        }
        validateEventComment(comment, eventId);
    }

    private void validateEventComment(Comment comment, Long eventId) {
        if (!comment.getEvent().getId().equals(eventId)) {
            throw new CommentValidationException("Cannot delete comment: wrong event");
        }
    }
}
