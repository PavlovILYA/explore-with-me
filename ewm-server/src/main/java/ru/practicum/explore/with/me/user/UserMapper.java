package ru.practicum.explore.with.me.user;

import ru.practicum.explore.with.me.user.dto.UserDto;
import ru.practicum.explore.with.me.user.dto.UserShortDto;
import ru.practicum.explore.with.me.user.model.User;

public class UserMapper {
    public static User toUser(UserDto userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name((user.getName()))
                .build();
    }

    public static UserShortDto userShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
