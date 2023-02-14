package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RequestControllerUnitTest {
    @Autowired
    private RequestController requestController;

    @Autowired
    private UserController userController;

    private RequestDto requestDto;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        requestDto = RequestDto.builder().description("description").build();
        userDto = UserDto.builder().name("user1").email("user1@user.com").build();
    }

    @Test
    @Transactional // test fails without this annotation
    void addRequest() {
        UserDto user = userController.addUser(userDto);
        RequestDto request = requestController.addRequest(user.getId(), requestDto);
        assertEquals(1, requestController.getRequest(user.getId(), request.getId()).getId());
        assertEquals("description",
                requestController.getRequest(user.getId(), request.getId()).getDescription());
    }

    @Test
    void addRequestWrongUser() {
        assertThrows(NotFoundException.class, () -> requestController.addRequest(99, requestDto));
    }

    @Test
    @Transactional
    void getRequestsByOwnerId() {
        UserDto userDto1 = userController.addUser(userDto);
        requestController.addRequest(userDto1.getId(), requestDto);
        assertEquals(1, requestController.getRequestsByOwnerId(userDto1.getId()).size());
    }

    @Test
    @Transactional
    void getRequestsByOwnerIdWrongOwnerId() {
        assertThrows(NotFoundException.class, () -> requestController.getRequestsByOwnerId(1));
    }

    @Test
    @Transactional
    void getAllRequestsCreatedByOtherUsers() {
        UserDto userDto1 = userController.addUser(userDto);
        requestController.addRequest(userDto1.getId(), requestDto);
        assertEquals(0, requestController.getAllRequestsCreatedByOtherUsers(1, 0, 100).size());
        userDto.setEmail("user2@user.com");
        UserDto userDto2 = userController.addUser(userDto);
        requestDto.setDescription("description2");
        requestController.addRequest(userDto2.getId(), requestDto);
        userDto.setEmail("user3@user.com");
        UserDto userDto3 = userController.addUser(userDto);
        requestDto.setDescription("description3");
        requestController.addRequest(userDto3.getId(), requestDto);
        assertEquals(2, requestController.getAllRequestsCreatedByOtherUsers(1, 0, 100).size());
    }

    @Test
    @Transactional
    void getAllRequestsCreatedByOtherUsersWrongUser() {
        assertThrows(NotFoundException.class,
                () -> requestController.getAllRequestsCreatedByOtherUsers(99, 0, 100));
    }

    @Test
    @Transactional
    void getAllRequestsCreatedByOtherUsersWrongSize() {
        UserDto userDto1 = userController.addUser(userDto);
        requestController.addRequest(userDto1.getId(), requestDto);
        assertThrows(IllegalArgumentException.class,
                () -> requestController.getAllRequestsCreatedByOtherUsers(1, 0, -100));
    }

    @Test
    @Transactional
    void getAllRequestsCreatedByOtherUsersWrongFrom() {
        UserDto userDto1 = userController.addUser(userDto);
        requestController.addRequest(userDto1.getId(), requestDto);
        assertThrows(IllegalArgumentException.class,
                () -> requestController.getAllRequestsCreatedByOtherUsers(1, -100, 10));
    }

}






