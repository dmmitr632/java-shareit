package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.exception.WrongStateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              UserRepository userRepository, ItemRepository itemRepository) {
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

        if (!item.isAvailable()) {
            throw new ValidationException("Item is not available");
        }
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        return bookingRepository.save(booking);
    }

    @Override
    public Booking approveOrRejectBooking(int userId, int bookingId, Boolean approvedOrNot) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(NotFoundException::new);
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new ValidationException("Already approved");
        }

        if (userId != booking.getItem().getOwner().getId()) {
            throw new NotFoundException("Only owner can approve or reject booking");
        } else if (approvedOrNot) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingRepository.save(booking);
    }


    @Override
    public Booking getBookingById(int userId, int bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(NotFoundException::new);
        if (userId != booking.getBooker().getId() && userId != booking.getItem().getOwner().getId()) {
            throw new NotFoundException("user is not booker or item owner");
        } else {
            return booking;
        }
    }

    @Override
    public List<Booking> getBookingByBookerId(int userId, String state, Integer from, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("user not found");
        }
        if (size == null) {
            size = Integer.MAX_VALUE;
        }
        Pageable pageable = PageRequest.of(from, size);
        if (Objects.equals(state, "ALL")) {
            return bookingRepository.findAllByBookerIdOrderByStartDesc(userId, pageable).stream().collect(Collectors.toList());
        } else if (Objects.equals(state, "CURRENT")) {
            return bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId,
                    LocalDateTime.now(), LocalDateTime.now(), pageable).stream().collect(Collectors.toList());
        } else if (Objects.equals(state, "PAST")) {
            return bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now(), pageable).stream().collect(Collectors.toList());
        } else if (Objects.equals(state, "FUTURE")) {
            return bookingRepository.findAllByBookerIdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now(), pageable).stream().collect(Collectors.toList());
        } else if (Objects.equals(state, "WAITING")) {
            return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING, pageable).stream().collect(Collectors.toList());
        } else if (Objects.equals(state, "REJECTED")) {
            return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED, pageable).stream().collect(Collectors.toList());
        } else {
            throw new WrongStateException("Unknown state: " + state);
        }
    }

    @Override
    public List<Booking> getBookingByOwnerId(int userId, String state, Integer from, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("owner not found");
        }
        if (size == null) {
            size = Integer.MAX_VALUE;
        }
        Pageable pageable = PageRequest.of(from, size);
        if (Objects.equals(state, "ALL")) {
            return bookingRepository.findAllByOwnerId(userId, pageable).stream().collect(Collectors.toList());
        } else if (Objects.equals(state, "CURRENT")) {
            return bookingRepository.findAllByOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId,
                    LocalDateTime.now(), LocalDateTime.now(), pageable).stream().collect(Collectors.toList());
        } else if (Objects.equals(state, "PAST")) {
            return bookingRepository.findAllByOwnerIdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now(), pageable).stream().collect(Collectors.toList());
        } else if (Objects.equals(state, "FUTURE")) {
            return bookingRepository.findAllByOwnerIdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now(), pageable).stream().collect(Collectors.toList());
        } else if (Objects.equals(state, "WAITING") || Objects.equals(state, "REJECTED")) {
            return bookingRepository.findAllByOwnerIdAndStatusEqualsOrderByStartDesc(userId,
                    BookingStatus.valueOf(state), pageable).stream().collect(Collectors.toList());
        } else {
            throw new WrongStateException("Unknown state: " + state);
        }
    }

    public void validateBooking(Booking booking) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (Objects.equals(booking.getBooker().getId(), booking.getItem().getOwner().getId())) {
            throw new NotFoundException("Booker and owner cannot be the same person");
        }
        if (booking.getEnd().isBefore(currentTime) || booking.getStart().isBefore(currentTime)) {
            throw new ValidationException("Booking time in before current time");
        }
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new ValidationException("Booking end time is before start time");
        }
    }

}
