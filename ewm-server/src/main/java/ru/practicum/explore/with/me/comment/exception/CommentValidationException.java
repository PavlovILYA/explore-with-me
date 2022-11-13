package ru.practicum.explore.with.me.comment.exception;

public class CommentValidationException extends RuntimeException {
    public CommentValidationException(String message) {
        super(message);
    }
}
