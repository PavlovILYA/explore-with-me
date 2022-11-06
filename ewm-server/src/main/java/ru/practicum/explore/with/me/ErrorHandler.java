package ru.practicum.explore.with.me;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explore.with.me.category.exception.CategoryNotFoundException;
import ru.practicum.explore.with.me.comment.exception.CommentNotFoundException;
import ru.practicum.explore.with.me.comment.exception.CommentValidationException;
import ru.practicum.explore.with.me.compilation.exception.CompilationNotFoundException;
import ru.practicum.explore.with.me.event.exception.EventCancelException;
import ru.practicum.explore.with.me.event.exception.EventNotFoundException;
import ru.practicum.explore.with.me.event.exception.EventValidationException;
import ru.practicum.explore.with.me.request.exception.RequestNotFoundException;
import ru.practicum.explore.with.me.request.exception.RequestValidationException;
import ru.practicum.explore.with.me.user.exception.UserNotFoundException;

import java.time.LocalDateTime;

import static ru.practicum.explore.with.me.Constants.formatter;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({UserNotFoundException.class,
                       CategoryNotFoundException.class,
                       EventNotFoundException.class,
                       RequestNotFoundException.class,
                       CompilationNotFoundException.class,
                       CommentNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle404Exception(final Exception e) {
        log.error("{} {}", HttpStatus.NOT_FOUND, e.getMessage());
        return ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.name())
                .reason("The required object was not found.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
    }

    @ExceptionHandler({EventCancelException.class,
                       EventValidationException.class,
                       RequestValidationException.class,
                       CommentValidationException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handle403Exception(final Exception e) {
        log.error("{} {}", HttpStatus.FORBIDDEN, e.getMessage());
        return ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.name())
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle500Exception(final Exception e) {
        log.error("{} {}", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .reason("Error occurred")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
    }
}
