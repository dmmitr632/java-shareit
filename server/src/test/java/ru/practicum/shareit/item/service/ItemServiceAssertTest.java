package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemQueueInfo;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemServiceAssertTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;

    private ItemShortDto itemShortDto;
    private CommentDto comment;
    private UserDto userDto;

    private final LocalDateTime week = LocalDateTime.of(2024, 2, 14, 12, 0);

    @BeforeEach
    void setUp() {
        itemShortDto = ItemShortDto.builder().name("name").description("description").available(true).build();
        RequestDto.builder().description("description").build();
        userDto = UserDto.builder().name("name").email("user1@user.com").build();
        comment = CommentDto.builder().text("comment").build();
    }

    @Test
    void addItem() {
        UserDto user = userService.addUser(userDto);
        ItemDto item = itemService.addItem(1, itemShortDto);
        assertEquals(item.getId(), itemService.getItemById(item.getId(), user.getId()).getId());
    }

    @Test
    void addItemWrongUser() {
        assertThrows(NotFoundException.class, () -> itemService.addItem(1, itemShortDto));
    }

    @Test
    void addUserWrongRequest() {
        itemShortDto.setRequestId(99);
        userService.addUser(userDto);
        assertThrows(NotFoundException.class, () -> itemService.addItem(1, itemShortDto));
    }

    @Test
    void editItem() {
        userService.addUser(userDto);
        itemService.addItem(1, itemShortDto);
        ItemShortDto itemShort = ItemShortDto.builder()
                .name("item edited")
                .ownerId(1)
                .description("description edited")
                .available(true)
                .build();
        itemService.editItem(1, 1, itemShort);
        assertEquals(itemShort.getName(), itemService.getItemById(1, 1).getName());
    }

    @Test
    void editItemWrongUser() {
        userService.addUser(userDto);
        itemService.addItem(1, itemShortDto);
        itemShortDto.setName("name edited");
        assertThrows(NotFoundException.class, () -> itemService.editItem(1, 99, itemShortDto));
    }

    @Test
    void editItemWrongItem() {
        assertThrows(NotFoundException.class, () -> itemService.editItem(1, 1, itemShortDto));
    }

    @Test
    void getItemsByTextSearch() {
        userService.addUser(userDto);
        itemService.addItem(1, itemShortDto);
        assertEquals(1, itemService.getItemsByTextSearch("cri", 0, 100).size());
    }

    @Test
    void getItemsByTextSearchWrongFrom() {
        assertThrows(IllegalArgumentException.class,
                () -> itemService.getItemsByTextSearch("text", -100, 10));
    }

    @Test
    void getItemsByTextSearchEmptyText() {
        userService.addUser(userDto);
        itemService.addItem(1, itemShortDto);
        assertEquals(new ArrayList<ItemDto>(), itemService.getItemsByTextSearch("", 0, 10));
    }

    @Test
    void addComment() throws InterruptedException {
        userService.addUser(userDto);
        ItemDto item = itemService.addItem(1, itemShortDto);
        userDto.setEmail("user@user.com");
        LocalDateTime currentTime = LocalDateTime.now();
        UserDto user2 = userService.addUser(userDto);
        bookingService.requestBooking(user2.getId(), BookingShortDto.builder()
                .start(currentTime.plusSeconds(1))
                .end(currentTime.plusSeconds(1))
                .itemId(item.getId())
                .build());
        bookingService.approveOrRejectBooking(1, 1, true);
        Thread.sleep(1000);
        itemService.addComment(user2.getId(), item.getId(), comment);
        assertEquals("comment", itemService.getItemById(1, 1).getComments().get(0).getText());

    }

    @Test
    void addCommentWrongItem() {
        userService.addUser(userDto);
        assertThrows(NotFoundException.class, () -> itemService.addComment(1, 1, comment));
        itemService.addItem(1, itemShortDto);
        assertThrows(ValidationException.class, () -> itemService.addComment(1, 1, comment));
    }

    @Test
    void addCommentWrongUser() {
        assertThrows(NotFoundException.class, () -> itemService.addComment(1, 1, comment));
    }

    @Test
    void getAllItemsByUserId() {
        UserDto user1 = userService.addUser(userDto);
        ItemDto itemDto = itemService.addItem(1, itemShortDto);
        assertEquals(itemDto, itemService.getAllItemsByUserId(user1.getId(), 0, 100).get(0));
    }

    @Test
    void getAllItemsByUserIdWrongFrom() {
        assertThrows(IllegalArgumentException.class, () -> itemService.getAllItemsByUserId(1, -100, 20));
    }

    @Test
    void getAllItemsByUserIdWrongSize() {
        assertThrows(IllegalArgumentException.class, () -> itemService.getAllItemsByUserId(1, 0, -10));
    }

    @Test
    void itemMapperMethods() {
        Item item = Item.builder().name("name1").description("description1").available(true).build();
        ItemShortDto itemShortDto1 = ItemMapper.toItemShortDto(item);
        assertEquals(itemShortDto1.getDescription(), ("description1"));
    }

    @Test
    void editItemWrongRequestId() {
        userService.addUser(userDto);
        itemService.addItem(1, itemShortDto);
        ItemShortDto itemShort = ItemShortDto.builder()
                .name("item edited")
                .ownerId(1)
                .description("description edited")
                .available(true)
                .requestId(1)
                .build();
        assertThrows(NotFoundException.class, () -> itemService.editItem(1, 1, itemShort));
    }

    @Test
    void getItemById() {
        userService.addUser(userDto);
        itemService.addItem(1, itemShortDto);
        userDto.setEmail("user2@user.com");
        userService.addUser(userDto);
        assertEquals("description", itemService.getItemById(1, 1).getDescription());
    }

    @Test
    void getItemByIdAnotherOwner() {
        userService.addUser(userDto);
        userDto.setId(2);
        userDto.setEmail("user2@user.com");
        userService.addUser(userDto);
        itemShortDto.setOwnerId(2);
        itemService.addItem(2, itemShortDto);
        assertEquals("description", itemService.getItemById(1, 1).getDescription());
    }

    @Test
    void toItemDtoFromQueueAndComments() {
        UserDto user1 = userService.addUser(userDto);
        itemService.addItem(user1.getId(), itemShortDto); //item id = 1

        ItemQueueInfo itemQueueInfo = new ItemQueueInfo() {
            @Override
            public Integer getId() {
                return 1;
            }

            @Override
            public String getName() {
                return "name";
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public Boolean getAvailable() {
                return true;
            }

            @Override
            public Integer getLastBookingId() {
                return 1;
            }

            @Override
            public Integer getLastBookingBookerId() {
                return null;
            }

            @Override
            public LocalDateTime getLastStart() {
                return null;
            }

            @Override
            public LocalDateTime getLastEnd() {
                return null;
            }

            @Override
            public Integer getNextBookingId() {
                return 2;
            }

            @Override
            public Integer getNextBookingBookerId() {
                return null;
            }

            @Override
            public LocalDateTime getNextStart() {
                return null;
            }

            @Override
            public LocalDateTime getNextEnd() {
                return null;
            }

            @Override
            public Integer getOwnerId() {
                return null;
            }
        };
        ItemDto itemDto2 = ItemMapper.toItemDtoFromQueueAndComments(itemQueueInfo, new ArrayList<>());
        assertEquals(itemDto2.getName(), "name");
    }

    @Test
    void toItemDtoFromQueueAndCommentsDto() {
        UserDto user1 = userService.addUser(userDto);
        itemService.addItem(user1.getId(), itemShortDto); //item id = 1

        ItemQueueInfo itemQueueInfo = new ItemQueueInfo() {
            @Override
            public Integer getId() {
                return 1;
            }

            @Override
            public String getName() {
                return "name";
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public Boolean getAvailable() {
                return true;
            }

            @Override
            public Integer getLastBookingId() {
                return 1;
            }

            @Override
            public Integer getLastBookingBookerId() {
                return null;
            }

            @Override
            public LocalDateTime getLastStart() {
                return null;
            }

            @Override
            public LocalDateTime getLastEnd() {
                return null;
            }

            @Override
            public Integer getNextBookingId() {
                return 2;
            }

            @Override
            public Integer getNextBookingBookerId() {
                return null;
            }

            @Override
            public LocalDateTime getNextStart() {
                return null;
            }

            @Override
            public LocalDateTime getNextEnd() {
                return null;
            }

            @Override
            public Integer getOwnerId() {
                return null;
            }
        };
        ItemDto itemDto2 = ItemMapper.toItemDtoFromQueueAndCommentsDto(itemQueueInfo, new ArrayList<>());
        assertEquals(itemDto2.getName(), "name");
    }

}