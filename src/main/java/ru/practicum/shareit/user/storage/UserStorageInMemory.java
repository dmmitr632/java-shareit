package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
@Component
@Qualifier("UserStorageInMemory")
public class UserStorageInMemory implements UserStorage {
    private Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @Override
    public User addUser(User user) {
        id++;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User editUser(int id, User user) {
        return users.put(user.getId(), user);
    }

    @Override
    public User getUserById(int id) {
        return users.get(id);

    }

    @Override
    public User deleteUser(int id) {
        return users.remove(id);
    }

    @Override
    public List<User> getAllUsers() {
        return (List<User>) users.values();
    }
}
