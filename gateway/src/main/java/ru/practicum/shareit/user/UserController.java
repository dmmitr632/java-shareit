package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserClient userClient;

    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserRequestDto userDto) {
        log.info("Add user");
        return userClient.addUser(userDto);
    }

    @PatchMapping("{userId}")
    public ResponseEntity<Object> editUser(@PathVariable long userId,
                                           @RequestBody UserRequestDto userDto) {
        log.info("Edit user with id {}", userId);
        return userClient.editUser(userId, userDto);
    }

    @GetMapping("{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable long userId) {
        log.info("Get user by id {}", userId);
        return userClient.getUserById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Get all users");
        return userClient.getAllUsers();
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        return userClient.deleteUser(userId);
    }
}
