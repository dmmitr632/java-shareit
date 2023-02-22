package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    UserDto editUser(int userId, UserDto userDto);

    UserDto getUserById(int userId);

    void deleteUser(int userId);

    List<UserDto> getAllUsers();

}
