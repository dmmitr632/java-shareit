package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;


public interface UserService {

    User addUser(User user);

    User editUser(int userId, UserDto userDto);

    User getUserById(int userId);

    User deleteUser(int userId);

    List<User> getAllUsers();

}
