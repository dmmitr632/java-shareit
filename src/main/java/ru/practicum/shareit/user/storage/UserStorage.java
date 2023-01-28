package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;


public interface UserStorage {
    User addUser(User user);

    User editUser(int id, User user);

    User getUserById(int id);

    User deleteUser(int id);

    List<User> getAllUsers();
}
