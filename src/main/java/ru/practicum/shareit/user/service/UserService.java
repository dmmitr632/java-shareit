package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(@Qualifier("UserStorageInMemory") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User editUser(int userId, UserDto userDto) {
        return userStorage.editUser(userId, UserMapper.toUser(userId, userDto, new User()));
    }

    public User getUserById(int userId) {
        return userStorage.getUserById(userId);
    }

    public User deleteUser(int userId) {
        return userStorage.deleteUser(userId);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }


}
