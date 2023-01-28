package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Repository
@Slf4j
@Component
@Qualifier("UserStorageInMemory")
public class UserStorageInMemory implements UserStorage {
    @Override
    public User addUser(User user) {
        return null;
    }

    @Override
    public User editUser(int id, User user) {
        return null;
    }

    @Override
    public User getUserById(int id) {
        return null;
    }

    @Override
    public User deleteUser(int id) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }
}
