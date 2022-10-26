package ru.practicum.explore.with.me.user.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User with id=" + id + " was not found");
    }
}
