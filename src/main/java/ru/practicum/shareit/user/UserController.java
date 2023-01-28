package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto addUser(@Valid @RequestBody User user) {
        return UserMapper.toUserDto(userService.addUser(user));
    }

    @PatchMapping("{userId}")
    public UserDto editUser(@PathVariable int userId, @RequestBody UserDto userDto) {
        return UserMapper.toUserDto(userService.editUser(userId, userDto));
    }

    @GetMapping("{userId}")
    public UserDto getUser(@PathVariable int userId) {
        return UserMapper.toUserDto(userService.getUserById(userId));
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        List<User> userList =  userService.getAllUsers();
        List<UserDto> usersDtoList = new ArrayList<>();
        userList.forEach(user -> usersDtoList.add(UserMapper.toUserDto(user)));
        return usersDtoList;
    }

    @DeleteMapping("{userId}")
    public UserDto deleteUser(@PathVariable int userId) {
        return UserMapper.toUserDto(userService.deleteUser(userId));
    }


}

