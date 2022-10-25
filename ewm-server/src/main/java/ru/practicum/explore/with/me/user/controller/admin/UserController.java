package ru.practicum.explore.with.me.user.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.with.me.user.UserMapper;
import ru.practicum.explore.with.me.user.dto.UserDto;
import ru.practicum.explore.with.me.user.model.User;
import ru.practicum.explore.with.me.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto saveUser(@RequestBody @Valid UserDto userDto) {
        log.info("POST /admin/users userDto: {}", userDto);
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userService.saveUser(user));
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam("ids") Long[] ids,
                                 @PositiveOrZero
                                 @RequestParam(value = "from", defaultValue = "0") int from,
                                 @PositiveOrZero
                                 @RequestParam(value = "size", defaultValue = "5") int size) {
        log.info("GET /admin/users ids{} from={} size={}", ids, from, size);
        return userService.getUsers(ids, from, size).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        log.info("DELETE /admin/users/{}", userId);
        userService.deleteUser(userId);
    }
}
