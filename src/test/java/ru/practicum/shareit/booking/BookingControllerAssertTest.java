package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingControllerAssertTest {

    @Autowired
    private BookingController bookingController;

    @Autowired
    private UserController userController;

    @Autowired
    private ItemController itemController;

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

        bookingShortDto = BookingShortDto.builder()
                .start(week)
                .end(nextWeek)
                .itemId(1)
                .build();
    }

    @Test
    void requestBooking() {
        UserDto user1 = userController.addUser(userDto1);
        itemController.addItem(user1.getId(), itemShortDto);
        UserDto user2 = userController.addUser(userDto2);
        BookingDto booking = bookingController.requestBooking(user2.getId(), bookingShortDto);
        assertEquals(1, bookingController.getBookingById(user2.getId(), booking.getId()).getId());
    }

    @Test
    void requestBookingWrongItem() {
        userController.addUser(userDto1);
        assertThrows(NotFoundException.class, () -> bookingController.requestBooking(1, bookingShortDto));
    }

    @Test
    void requestBookingByWrongUserId() {
        assertThrows(NotFoundException.class, () -> bookingController.requestBooking(1, bookingShortDto));
    }

    @Test
    void requestBookingByOwner() {
        UserDto user1 = userController.addUser(userDto1);
        itemController.addItem(user1.getId(), itemShortDto);
        assertThrows(NotFoundException.class, () -> bookingController.requestBooking(1, bookingShortDto));
    }

    @Test
    void requestBookingItemNotAvailable() {
        UserDto user1 = userController.addUser(userDto1);
        itemShortDto.setAvailable(false);
        itemController.addItem(user1.getId(), itemShortDto);
        userController.addUser(userDto2);
        assertThrows(ValidationException.class, () -> bookingController.requestBooking(2, bookingShortDto));
    }

    @Test
    void requestBookingWrongEndDateTime() {
        UserDto user1 = userController.addUser(userDto1);
        itemController.addItem(user1.getId(), itemShortDto);
        UserDto user2 = userController.addUser(userDto2);
        bookingShortDto.setEnd(LocalDateTime.of(2022, 9, 24, 12, 30));
        assertThrows(ValidationException.class,
                () -> bookingController.requestBooking(user2.getId(), bookingShortDto));
    }

    @Test
    void approveOrRejectBooking() {
        UserDto owner = userController.addUser(userDto1);
        UserDto booker = userController.addUser(userDto2);
        ItemDto item = itemController.addItem(owner.getId(), itemShortDto);
        BookingDto booking = bookingController.requestBooking(booker.getId(), BookingShortDto.builder()
                .start(week)
                .end(nextWeek)
                .itemId(item.getId())
                .build());
        assertEquals(BookingStatus.WAITING,
                bookingController.getBookingById(booker.getId(), booking.getId()).getStatus());
        bookingController.approveOrRejectBooking(owner.getId(), booking.getId(), false);
        assertEquals(BookingStatus.REJECTED,
                bookingController.getBookingById(booker.getId(), booking.getId()).getStatus());
        bookingController.approveOrRejectBooking(owner.getId(), booking.getId(), true);
        assertEquals(BookingStatus.APPROVED,
                bookingController.getBookingById(booker.getId(), booking.getId()).getStatus());
    }

    @Test
    void approveWrongUser() {
        UserDto user1 = userController.addUser(userDto1);
        itemController.addItem(user1.getId(), itemShortDto);
        UserDto user2 = userController.addUser(userDto2);
        bookingController.requestBooking(user2.getId(), bookingShortDto);
        assertThrows(NotFoundException.class, () -> bookingController.approveOrRejectBooking(1, 2, true));
    }

    @Test
    void approveWrongBooking() {
        assertThrows(NotFoundException.class, () -> bookingController.approveOrRejectBooking(1, 1, true));
    }

    @Test
    void approveWrongStatus() {
        UserDto user1 = userController.addUser(userDto1);
        itemController.addItem(user1.getId(), itemShortDto);
        UserDto user2 = userController.addUser(userDto2); // id =2
        bookingController.requestBooking(user2.getId(), bookingShortDto);
        bookingController.approveOrRejectBooking(1, 1, true);
        assertThrows(ValidationException.class, () -> bookingController.approveOrRejectBooking(1, 1, true));
    }

    @Test
    void getBookingByIdWrongUserId() {
        assertThrows(NotFoundException.class, () -> bookingController.getBookingById(1, 1));
    }

    @Test
    void getExistingBookingByIdWrongUserId() {
        UserDto user1 = userController.addUser(userDto1);
        itemController.addItem(user1.getId(), itemShortDto);
        UserDto user2 = userController.addUser(userDto2);
        bookingController.requestBooking(user2.getId(), bookingShortDto);
        assertThrows(NotFoundException.class, () -> bookingController.getBookingById(99, 1));
    }

    @Test
    void getBookingByBookerIdWrongUserId() {
        assertThrows(NotFoundException.class,
                () -> bookingController.getAllBookingsByBookerId(1, "ALL", 0, 100));
        assertThrows(NotFoundException.class,
                () -> bookingController.getAllBookingsByOwnerId(1, "ALL", 0, 100));
    }

    @Test
    void getBookingByBookerId() {
        UserDto user1 = userController.addUser(userDto1);
        itemController.addItem(user1.getId(), itemShortDto); //item id = 1
        UserDto user2 = userController.addUser(userDto2);
        BookingDto booking = bookingController.requestBooking(user2.getId(), bookingShortDto);
        assertEquals(1, bookingController.getAllBookingsByBookerId(user2.getId(), "WAITING", 0, 100).size());
        assertEquals(1, bookingController.getAllBookingsByBookerId(user2.getId(), "ALL", 0, 100).size());
        assertEquals(0, bookingController.getAllBookingsByBookerId(user2.getId(), "PAST", 0, 100).size());
        assertEquals(0, bookingController.getAllBookingsByBookerId(user2.getId(), "CURRENT", 0, 100).size());
        assertEquals(1, bookingController.getAllBookingsByBookerId(user2.getId(), "FUTURE", 0, 100).size());
        assertEquals(0, bookingController.getAllBookingsByBookerId(user2.getId(), "REJECTED", 0, 100).size());
        bookingController.approveOrRejectBooking(booking.getId(), user1.getId(), false);
        assertEquals(0, bookingController.getAllBookingsByOwnerId(user1.getId(), "CURRENT", 0, 100).size());
        assertEquals(1, bookingController.getAllBookingsByOwnerId(user1.getId(), "ALL", 0, 100).size());
        assertEquals(0, bookingController.getAllBookingsByOwnerId(user1.getId(), "WAITING", 0, 100).size());
        assertEquals(1, bookingController.getAllBookingsByOwnerId(user1.getId(), "FUTURE", 0, 100).size());
        assertEquals(1, bookingController.getAllBookingsByOwnerId(user1.getId(), "REJECTED", 0, 100).size());
        assertEquals(0, bookingController.getAllBookingsByOwnerId(user1.getId(), "PAST", 0, 100).size());
        bookingController.approveOrRejectBooking(booking.getId(), user1.getId(), true);
        assertEquals(0, bookingController.getAllBookingsByOwnerId(user1.getId(), "CURRENT", 0, 100).size());
        assertEquals(1, bookingController.getAllBookingsByOwnerId(user1.getId(), "ALL", 0, 100).size());
        assertEquals(0, bookingController.getAllBookingsByOwnerId(user1.getId(), "WAITING", 0, 100).size());
        assertEquals(1, bookingController.getAllBookingsByOwnerId(user1.getId(), "FUTURE", 0, 100).size());
        assertEquals(0, bookingController.getAllBookingsByOwnerId(user1.getId(), "REJECTED", 0, 100).size());
        assertEquals(0, bookingController.getAllBookingsByOwnerId(user1.getId(), "PAST", 0, 100).size());
    }

    @Test
    void getBookingByBookerIdWrongState() {
        UserDto user1 = userController.addUser(userDto1);
        itemController.addItem(user1.getId(), itemShortDto); //item id = 1
        UserDto user2 = userController.addUser(userDto2);
        bookingController.requestBooking(user2.getId(), bookingShortDto);
        assertEquals(1, bookingController.getAllBookingsByBookerId(user2.getId(), "WAITING", 0, 100).size());
        assertThrows(WrongStateException.class,
                () -> bookingController.getAllBookingsByBookerId(user2.getId(),
                        "UNKNOWN_STATE", 0, 100));
    }

    @Test
    void getBookingByOwnerIdWrongState() {
        UserDto user1 = userController.addUser(userDto1);
        itemController.addItem(user1.getId(), itemShortDto); //item id = 1
        UserDto user2 = userController.addUser(userDto2);
        bookingController.requestBooking(user2.getId(), bookingShortDto);
        assertEquals(1, bookingController.getAllBookingsByOwnerId(user1.getId(), "WAITING", 0, 100).size());
        assertThrows(WrongStateException.class, () -> bookingController.getAllBookingsByOwnerId(user1.getId(),
                "UNKNOWN_STATE", 0, 100));
    }

    @Test
    void equalsHashcode() {
        UserDto user1 = userController.addUser(userDto1);
        itemController.addItem(user1.getId(), itemShortDto); //item id = 1
        UserDto user2 = userController.addUser(userDto2);
        BookingDto booking = bookingController.requestBooking(user2.getId(), bookingShortDto);
        assertEquals(booking, booking);
        assertEquals(bookingShortDto, bookingShortDto);
    }
}