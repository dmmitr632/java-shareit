package ru.practicum.shareit.user;

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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerUnitTest {
    @Autowired
    private UserController userController;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder().name("name").email("user@user.com").build();
    }

    @Test
    void addUser() {
        UserDto userDto1 = userController.addUser(userDto);
        assertEquals(userDto1.getId(), userController.getUser(userDto1.getId()).getId());
    }

    @Test
    void editUser() {
        userController.addUser(userDto);
        userDto.setName("name edited");
        userController.editUser(1, userDto);
        assertEquals(userDto.getName(), userController.getUser(1).getName());
    }

    @Test
    void editUserWrongUser() {
        assertThrows(NotFoundException.class, () -> userController.editUser(99, userDto));
    }

    @Test
    void getUser() {
        assertThrows(NotFoundException.class, () -> userController.getUser(99));
    }

    @Test
    void deleteUser() {
        UserDto userDto1 = userController.addUser(
                UserDto.builder().name("user1").email("user1@user.com").build());
        assertEquals(1, userController.getAllUsers().size());
        userController.deleteUser(userDto1.getId());
        assertEquals(0, userController.getAllUsers().size());
    }
}
