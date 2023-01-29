package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
@Slf4j
@Component
@Qualifier("UserStorageInMemory")
public class UserStorageInMemory implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @Override
    public User addUser(User user) {
        checkIfEmailIsDuplicated(user.getEmail());
        id++;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User editUser(int id, User user) {
        if (users.containsKey(id)) {
            checkIfEditedEmailIsDuplicated(user.getEmail(), id);
            user.setId(id);
            if (user.getName() == null) user.setName(users.get(id).getName());
            if (user.getEmail() == null) user.setEmail(users.get(id).getEmail());
            users.put(id, user);
            return user;
        } else throw new ValidationException();
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
        return new ArrayList<>(users.values());
    }

    public void checkIfEditedEmailIsDuplicated(String email, int userId) {
        Optional<User> user = users.values().stream().filter(u -> u.getEmail().equals(email)).findFirst();
        if (user.isPresent() && user.orElse(null).getId() != userId) {
            throw new ValidationException();
        }
    }


    public void checkIfEmailIsDuplicated(String email) {
        Optional<User> user = users.values().stream().filter(u -> u.getEmail().equals(email)).findFirst();
        if (user.isPresent()) {
            throw new ValidationException();
        }
    }
}
