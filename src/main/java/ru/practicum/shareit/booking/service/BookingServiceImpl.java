package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public Booking requestBooking(int userId, BookingDto bookingDto) {
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(NotFoundException::new);
        User booker = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Booking booking = BookingMapper.toBooking(bookingDto, booker, item);
        validateBooking(booking);

        int itemId = item.getId();
        if (!itemRepository.findById(itemId).orElseThrow(NotFoundException::new).isAvailable()) {
            throw new ValidationException("Item is not available");
        }
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        System.out.println("booking" + booking);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking approveOrRejectBooking(int userId, int bookingId, Boolean approvedOrNot) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(NotFoundException::new);

        if (userId != booking.getItem().getOwner().getId()) {
            return booking;
        } else if (approvedOrNot) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return booking;
    }


    @Override
    public Booking getBookingById(int userId, int bookingId) {
        return null;
    }

    @Override
    public List<BookingDto> getBookingByBookerId(int userId, String state) {
        return null;
    }

    @Override
    public List<BookingDto> getBookingByOwnerId(int userId, String state) {
        return null;
    }

    public void validateBooking(Booking booking) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (booking.getEnd().isBefore(currentTime) || booking.getStart().isBefore(currentTime)) {
            throw new ValidationException("Booking time in before current time");
        }
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new ValidationException("Booking end time is before start time");
        }
    }

}
