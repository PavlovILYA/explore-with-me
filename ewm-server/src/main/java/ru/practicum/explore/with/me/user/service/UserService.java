package ru.practicum.explore.with.me.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.with.me.user.exception.UserNotFoundException;
import ru.practicum.explore.with.me.user.model.User;
import ru.practicum.explore.with.me.user.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User saveUser(User user) {
        User savedUser = userRepository.save(user);
        log.info("Saved user: {}", savedUser);
        return savedUser;
    }

    public List<User> getUsers(Long[] ids, int from, int size) {
        Pageable pageRequest = PageRequest.of(from / size, size);
        List<User> returnedUsers = userRepository.findUserByIdIn(Arrays.asList(ids), pageRequest).getContent();
        log.info("Returned users: {}", returnedUsers);
        return returnedUsers;
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
        log.info("User {} is deleted", userId);
    }

    public User getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        log.info("Get user {}", userId);
        return user;
    }
}
