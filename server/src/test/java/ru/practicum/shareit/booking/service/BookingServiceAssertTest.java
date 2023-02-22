package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.exception.WrongStateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

public class BookingServiceAssertTest {
    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;
    private ItemShortDto itemShortDto;

    private UserDto userDto1;

    private UserDto userDto2;

    private BookingShortDto bookingShortDto;

    private final LocalDateTime week = LocalDateTime.of(2024, 2, 14, 12, 0);
    private final LocalDateTime nextWeek = week.plusWeeks(1);

    @BeforeEach
    void setUp() {
        itemShortDto = ItemShortDto.builder().name("name").description("description").available(true).build();

        userDto1 = UserDto.builder().name("name").email("user1@gmail.com").build();

        userDto2 = UserDto.builder().name("name").email("user2@gmail.com").build();

        bookingShortDto = BookingShortDto.builder().start(week).end(nextWeek).itemId(1).build();
    }

    @Test
    void requestBooking() {
        UserDto user1 = userService.addUser(userDto1);
        itemService.addItem(user1.getId(), itemShortDto);
        UserDto user2 = userService.addUser(userDto2);
        BookingDto booking = bookingService.requestBooking(user2.getId(), bookingShortDto);
        assertEquals(1, bookingService.getBookingById(user2.getId(), booking.getId()).getId());
    }

    @Test
    void requestBookingWrongItem() {
        userService.addUser(userDto1);
        assertThrows(NotFoundException.class, () -> bookingService.requestBooking(1, bookingShortDto));
    }

    @Test
    void requestBookingByWrongUserId() {
        assertThrows(NotFoundException.class, () -> bookingService.requestBooking(1, bookingShortDto));
    }

    @Test
    void requestBookingByOwner() {
        UserDto user1 = userService.addUser(userDto1);
        itemService.addItem(user1.getId(), itemShortDto);
        assertThrows(NotFoundException.class, () -> bookingService.requestBooking(1, bookingShortDto));
    }

    @Test
    void requestBookingItemNotAvailable() {
        UserDto user1 = userService.addUser(userDto1);
        itemShortDto.setAvailable(false);
        itemService.addItem(user1.getId(), itemShortDto);
        userService.addUser(userDto2);
        assertThrows(ValidationException.class, () -> bookingService.requestBooking(2, bookingShortDto));
    }

    @Test
    void approveOrRejectBooking() {
        UserDto owner = userService.addUser(userDto1);
        UserDto booker = userService.addUser(userDto2);
        ItemDto item = itemService.addItem(owner.getId(), itemShortDto);
        BookingDto booking = bookingService.requestBooking(booker.getId(),
                BookingShortDto.builder().start(week).end(nextWeek).itemId(item.getId()).build());
        assertEquals(BookingStatus.WAITING,
                bookingService.getBookingById(booker.getId(), booking.getId()).getStatus());
        bookingService.approveOrRejectBooking(owner.getId(), booking.getId(), false);
        assertEquals(BookingStatus.REJECTED,
                bookingService.getBookingById(booker.getId(), booking.getId()).getStatus());
        bookingService.approveOrRejectBooking(owner.getId(), booking.getId(), true);
        assertEquals(BookingStatus.APPROVED,
                bookingService.getBookingById(booker.getId(), booking.getId()).getStatus());
    }

    @Test
    void approveWrongUser() {
        UserDto user1 = userService.addUser(userDto1);
        itemService.addItem(user1.getId(), itemShortDto);
        UserDto user2 = userService.addUser(userDto2);
        bookingService.requestBooking(user2.getId(), bookingShortDto);
        assertThrows(NotFoundException.class, () -> bookingService.approveOrRejectBooking(1, 2, true));
    }

    @Test
    void approveWrongBooking() {
        assertThrows(NotFoundException.class, () -> bookingService.approveOrRejectBooking(1, 1, true));
    }

    @Test
    void approveWrongStatus() {
        UserDto user1 = userService.addUser(userDto1);
        itemService.addItem(user1.getId(), itemShortDto);
        UserDto user2 = userService.addUser(userDto2); // id =2
        bookingService.requestBooking(user2.getId(), bookingShortDto);
        bookingService.approveOrRejectBooking(1, 1, true);
        assertThrows(ValidationException.class, () -> bookingService.approveOrRejectBooking(1, 1, true));
    }

    @Test
    void getBookingByIdWrongUserId() {
        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(1, 1));
    }

    @Test
    void getExistingBookingByIdWrongUserId() {
        UserDto user1 = userService.addUser(userDto1);
        itemService.addItem(user1.getId(), itemShortDto);
        UserDto user2 = userService.addUser(userDto2);
        bookingService.requestBooking(user2.getId(), bookingShortDto);
        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(99, 1));
    }

    @Test
    void getBookingByBookerIdWrongUserId() {
        assertThrows(NotFoundException.class,
                () -> bookingService.getAllBookingsByBookerId(1, "ALL", 0, 100));
        assertThrows(NotFoundException.class, () -> bookingService.getAllBookingsByOwnerId(1, "ALL", 0, 100));
    }

    @Test
    void getBookingByBookerId() {
        UserDto user1 = userService.addUser(userDto1);
        itemService.addItem(user1.getId(), itemShortDto); //item id = 1
        UserDto user2 = userService.addUser(userDto2);
        BookingDto booking = bookingService.requestBooking(user2.getId(), bookingShortDto);
        assertEquals(1, bookingService.getAllBookingsByBookerId(user2.getId(), "WAITING", 0, 100).size());
        assertEquals(1, bookingService.getAllBookingsByBookerId(user2.getId(), "ALL", 0, 100).size());
        assertEquals(0, bookingService.getAllBookingsByBookerId(user2.getId(), "PAST", 0, 100).size());
        assertEquals(0, bookingService.getAllBookingsByBookerId(user2.getId(), "CURRENT", 0, 100).size());
        assertEquals(1, bookingService.getAllBookingsByBookerId(user2.getId(), "FUTURE", 0, 100).size());
        assertEquals(0, bookingService.getAllBookingsByBookerId(user2.getId(), "REJECTED", 0, 100).size());
        bookingService.approveOrRejectBooking(booking.getId(), user1.getId(), false);
        assertEquals(0, bookingService.getAllBookingsByOwnerId(user1.getId(), "CURRENT", 0, 100).size());
        assertEquals(1, bookingService.getAllBookingsByOwnerId(user1.getId(), "ALL", 0, 100).size());
        assertEquals(0, bookingService.getAllBookingsByOwnerId(user1.getId(), "WAITING", 0, 100).size());
        assertEquals(1, bookingService.getAllBookingsByOwnerId(user1.getId(), "FUTURE", 0, 100).size());
        assertEquals(1, bookingService.getAllBookingsByOwnerId(user1.getId(), "REJECTED", 0, 100).size());
        assertEquals(0, bookingService.getAllBookingsByOwnerId(user1.getId(), "PAST", 0, 100).size());
        bookingService.approveOrRejectBooking(booking.getId(), user1.getId(), true);
        assertEquals(0, bookingService.getAllBookingsByOwnerId(user1.getId(), "CURRENT", 0, 100).size());
        assertEquals(1, bookingService.getAllBookingsByOwnerId(user1.getId(), "ALL", 0, 100).size());
        assertEquals(0, bookingService.getAllBookingsByOwnerId(user1.getId(), "WAITING", 0, 100).size());
        assertEquals(1, bookingService.getAllBookingsByOwnerId(user1.getId(), "FUTURE", 0, 100).size());
        assertEquals(0, bookingService.getAllBookingsByOwnerId(user1.getId(), "REJECTED", 0, 100).size());
        assertEquals(0, bookingService.getAllBookingsByOwnerId(user1.getId(), "PAST", 0, 100).size());
    }

    @Test
    void getBookingByBookerIdWrongState() {
        UserDto user1 = userService.addUser(userDto1);
        itemService.addItem(user1.getId(), itemShortDto); //item id = 1
        UserDto user2 = userService.addUser(userDto2);
        bookingService.requestBooking(user2.getId(), bookingShortDto);
        assertEquals(1, bookingService.getAllBookingsByBookerId(user2.getId(), "WAITING", 0, 100).size());
        assertThrows(WrongStateException.class,
                () -> bookingService.getAllBookingsByBookerId(user2.getId(), "UNKNOWN_STATE", 0, 100));
    }

    @Test
    void getBookingByOwnerIdWrongState() {
        UserDto user1 = userService.addUser(userDto1);
        itemService.addItem(user1.getId(), itemShortDto); //item id = 1
        UserDto user2 = userService.addUser(userDto2);
        bookingService.requestBooking(user2.getId(), bookingShortDto);
        assertEquals(1, bookingService.getAllBookingsByOwnerId(user1.getId(), "WAITING", 0, 100).size());
        assertThrows(WrongStateException.class,
                () -> bookingService.getAllBookingsByOwnerId(user1.getId(), "UNKNOWN_STATE", 0, 100));
    }
}