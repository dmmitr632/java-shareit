package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemControllerUnitTest {
    @Autowired
    private ItemController itemController;
    @Autowired
    private UserController userController;
    @Autowired
    private BookingController bookingController;

    private ItemShortDto itemShortDto;
    private CommentDto comment;
    private UserDto userDto;

    private final LocalDateTime week = LocalDateTime.of(2024, 2, 14, 12, 0);
    private final LocalDateTime nextWeek = week.plusWeeks(1);

    @BeforeEach
    void setUp() {
        itemShortDto = ItemShortDto.builder().name("name").description("description").available(true).build();
        RequestDto requestDto = RequestDto.builder().description("description").build();
        userDto = UserDto.builder().name("name").email("user1@user.com").build();
        comment = CommentDto.builder().text("comment").build();
    }

    @Test
    void addItem() {
        UserDto user = userController.addUser(userDto);
        ItemDto item = itemController.addItem(1, itemShortDto);
        assertEquals(item.getId(), itemController.getItemById(item.getId(), user.getId()).getId());
    }

    @Test
    void addItemWrongUser() {
        assertThrows(NotFoundException.class, () -> itemController.addItem(1, itemShortDto));
    }

    @Test
    void addUserWrongRequest() {
        itemShortDto.setRequestId(99);
        UserDto user = userController.addUser(userDto);
        assertThrows(NotFoundException.class, () -> itemController.addItem(1, itemShortDto));
    }

    @Test
    void editItem() {
        userController.addUser(userDto);
        itemController.addItem(1, itemShortDto);
        ItemShortDto itemShort = ItemShortDto.builder()
                .name("item edited")
                .ownerId(1)
                .description("description edited")
                .available(true)
                .build();
        itemController.editItem(1, 1, itemShort);
        assertEquals(itemShort.getName(), itemController.getItemById(1, 1).getName());
    }

    @Test
    void editItemWrongUser() {
        userController.addUser(userDto);
        itemController.addItem(1, itemShortDto);
        itemShortDto.setName("name edited");
        assertThrows(NotFoundException.class, () -> itemController.editItem(1, 99, itemShortDto));
    }

    @Test
    void editItemWrongItem() {
        assertThrows(NotFoundException.class, () -> itemController.editItem(1, 1, itemShortDto));
    }

    @Test
    void getItemsByTextSearch() {
        userController.addUser(userDto);
        itemController.addItem(1, itemShortDto);
        assertEquals(1, itemController.getItemsByTextSearch("cri", 0, 100).size());
    }

    @Test
    void getItemsByTextSearchWrongFrom() {
        assertThrows(IllegalArgumentException.class,
                () -> itemController.getItemsByTextSearch("text", -100, 10));
    }

    @Test
    void getItemsByTextSearchEmptyText() {
        userController.addUser(userDto);
        itemController.addItem(1, itemShortDto);
        assertEquals(new ArrayList<ItemDto>(), itemController.getItemsByTextSearch("", 0, 10));
    }

    @Test
    void addComment() throws InterruptedException {
        UserDto user1 = userController.addUser(userDto);
        ItemDto item = itemController.addItem(1, itemShortDto);
        userDto.setEmail("user@user.com");
        LocalDateTime currentTime = LocalDateTime.now();
        UserDto user2 = userController.addUser(userDto);
        bookingController.requestBooking(user2.getId(), BookingShortDto.builder()
                .start(currentTime.plusSeconds(1))
                .end(currentTime.plusSeconds(1))
                .itemId(item.getId())
                .build());
        bookingController.approveOrRejectBooking(1, 1, true);
        Thread.sleep(1000);
        itemController.addComment(user2.getId(), item.getId(), comment);
        assertEquals("comment", itemController.getItemById(1, 1).getComments().get(0).getText());

    }

    @Test
    void addCommentWrongItem() {
        UserDto user = userController.addUser(userDto);
        assertThrows(NotFoundException.class, () -> itemController.addComment(1, 1, comment));
        ItemDto item = itemController.addItem(1, itemShortDto);
        assertThrows(ValidationException.class, () -> itemController.addComment(1, 1, comment));
    }

    @Test
    void addCommentWrongUser() {
        assertThrows(NotFoundException.class, () -> itemController.addComment(1, 1, comment));
    }

    @Test
    void getAllItemsByUserId() {
        UserDto user1 = userController.addUser(userDto);
        ItemDto itemDto = itemController.addItem(1, itemShortDto);
        assertEquals(itemDto, itemController.getAllItemsByUserId(user1.getId(), 0, 100).get(0));
    }

    @Test
    void getAllItemsByUserIdWrongFrom() {
        assertThrows(IllegalArgumentException.class, () -> itemController.getAllItemsByUserId(1, -100, 20));
    }

    @Test
    void getAllItemsByUserIdWrongSize() {
        assertThrows(IllegalArgumentException.class, () -> itemController.getAllItemsByUserId(1, 0, -10));
    }

    @Test
    void itemMapperMethods() {
        Item item = Item.builder().name("name1").description("description1").available(true).build();
        ItemShortDto itemShortDto1 = ItemMapper.toItemShortDto(item);
        assertEquals(itemShortDto1.getDescription(), ("description1"));
    }



    @Test
    void editItemWrongRequestId() {
        userController.addUser(userDto);
        itemController.addItem(1, itemShortDto);
        ItemShortDto itemShort = ItemShortDto.builder()
                .name("item edited")
                .ownerId(1)
                .description("description edited")
                .available(true)
                .requestId(1)
                .build();
        assertThrows(NotFoundException.class, () -> itemController.editItem(1, 1, itemShort));
    }

    @Test
    void getItemById() {
        userController.addUser(userDto);
        itemController.addItem(1, itemShortDto);
        userDto.setEmail("user2@user.com");
        userController.addUser(userDto);
        assertEquals("description", itemController.getItemById(1 , 1).getDescription());
    }

    @Test
    void getItemByIdAnotherOwner() {
        userController.addUser(userDto);
        userDto.setId(2);
        userDto.setEmail("user2@user.com");
        userController.addUser(userDto);
        itemShortDto.setOwnerId(2);
        itemController.addItem(2, itemShortDto);
        assertEquals("description", itemController.getItemById(1 , 1).getDescription());
    }

}