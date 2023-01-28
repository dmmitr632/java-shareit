package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.List;


public interface UserStorage {
    User addUser(User user);

    User editUser(int id, User user);

    User getUserById(int id);

    User deleteUser(int id);

    List<User> getAllUsers();
}
