package ru.practicum.explore.with.me.request.exception;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(Long id) {
        super("Request with id=" + id + " was not found");
    }
}
