package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RequestServiceAssertTest {
    @Autowired
    private RequestService requestService;

    @Autowired
    private UserService userService;

    private RequestDto requestDto;

    private UserDto userDto;
    private final LocalDateTime week = LocalDateTime.of(2024, 2, 14, 12, 0);

    @BeforeEach
    void setUp() {
        requestDto = RequestDto.builder().description("description").build();
        userDto = UserDto.builder().name("user1").email("user1@user.com").build();
    }

    @Test
    @Transactional
        // test fails without this annotation
    void addRequest() {
        UserDto user = userService.addUser(userDto);
        RequestDto request = requestService.addRequest(requestDto, user.getId());
        assertEquals(1, requestService.getRequestById(user.getId(), request.getId()).getId());
        assertEquals("description",
                requestService.getRequestById(user.getId(), request.getId()).getDescription());
    }

    @Test
    void addRequestWrongUser() {
        assertThrows(NotFoundException.class, () -> requestService.addRequest(requestDto, 99));
    }

    @Test
    @Transactional
    void getRequestsByUserId() {
        UserDto userDto1 = userService.addUser(userDto);
        requestService.addRequest(requestDto, userDto1.getId());
        assertEquals(1, requestService.getRequestsByUserId(userDto1.getId()).size());
    }

    @Test
    @Transactional
    void getRequestsByUserIdWrongOwnerId() {
        assertThrows(NotFoundException.class, () -> requestService.getRequestsByUserId(1));
    }

    @Test
    @Transactional
    void getRequestsOfOtherUsers() {
        UserDto userDto1 = userService.addUser(userDto);
        requestService.addRequest(requestDto, userDto1.getId());
        assertEquals(0, requestService.getRequestsOfOtherUsers(1, 0, 100).size());
        userDto.setEmail("user2@user.com");
        UserDto userDto2 = userService.addUser(userDto);
        requestDto.setDescription("description2");
        requestService.addRequest(requestDto, userDto2.getId());
        userDto.setEmail("user3@user.com");
        UserDto userDto3 = userService.addUser(userDto);
        requestDto.setDescription("description3");
        requestService.addRequest(requestDto, userDto3.getId());
        assertEquals(2, requestService.getRequestsOfOtherUsers(1, 0, 100).size());
    }

    @Test
    @Transactional
    void getRequestsOfOtherUsersWrongUser() {
        assertThrows(NotFoundException.class, () -> requestService.getRequestsOfOtherUsers(99, 0, 100));
    }

    @Test
    @Transactional
    void getRequestsOfOtherUsersWrongSize() {
        UserDto userDto1 = userService.addUser(userDto);
        requestService.addRequest(requestDto, userDto1.getId());
        assertThrows(IllegalArgumentException.class,
                () -> requestService.getRequestsOfOtherUsers(1, 0, -100));
    }

    @Test
    @Transactional
    void getRequestsOfOtherUsersWrongFrom() {
        UserDto userDto1 = userService.addUser(userDto);
        requestService.addRequest(requestDto, userDto1.getId());
        assertThrows(IllegalArgumentException.class,
                () -> requestService.getRequestsOfOtherUsers(1, -100, 10));
    }

    @Test
    void requestMapperTest() {
        requestDto.setCreated(week);
        UserDto userDto1 = userService.addUser(userDto);
        Request request = RequestMapper.toRequestWithoutItems(requestDto, UserMapper.toUser(userDto1));
        assertEquals(request.getCreated(), week);
    }
}






