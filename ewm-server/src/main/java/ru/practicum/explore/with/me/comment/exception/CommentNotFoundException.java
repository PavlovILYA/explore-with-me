package ru.practicum.explore.with.me.comment.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(Long id) {
        super("Comment with id=" + id + " was not found");
    }
}
