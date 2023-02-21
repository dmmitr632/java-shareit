package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceAssertTest {
    @Autowired
    private UserService userService;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder().name("name").email("user@user.com").build();
    }

    @Test
    void addUser() {
        UserDto userDto1 = userService.addUser(userDto);
        assertEquals(userDto1.getId(), userService.getUserById(userDto1.getId()).getId());
    }

    @Test
    void editUser() {
        userService.addUser(userDto);
        userDto.setName("name edited");
        userService.editUser(1, userDto);
        assertEquals(userDto.getName(), userService.getUserById(1).getName());
    }

    @Test
    void editUserWrongUser() {
        assertThrows(NotFoundException.class, () -> userService.editUser(99, userDto));
    }

    @Test
    void getUserById() {
        assertThrows(NotFoundException.class, () -> userService.getUserById(99));
    }

    @Test
    void deleteUser() {
        UserDto userDto1 = userService.addUser(
                UserDto.builder().name("user1").email("user1@user.com").build());
        assertEquals(1, userService.getAllUsers().size());
        userService.deleteUser(userDto1.getId());
        assertEquals(0, userService.getAllUsers().size());
    }
}
