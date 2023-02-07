package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithItemAndBookerDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping()
    public BookingWithItemAndBookerDto requestBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                                      @RequestBody @Valid BookingDto bookingDto) {
        return BookingMapper.toBookingWithItemAndBookerDto(bookingService.requestBooking(userId, bookingDto));
    }

    @PatchMapping("/{bookingId}")
    public BookingWithItemAndBookerDto approveOrRejectBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                                              @PathVariable int bookingId,
                                                              @RequestParam(value = "approved") Boolean approved) {
        return BookingMapper.toBookingWithItemAndBookerDto(bookingService.approveOrRejectBooking(userId, bookingId,
                approved));
    }

    @GetMapping("/{bookingId}")
    public BookingWithItemAndBookerDto getBookingById(@RequestHeader("X-Sharer-User-Id") int userId,
                                                      @PathVariable int bookingId) {
        return BookingMapper.toBookingWithItemAndBookerDto(bookingService.getBookingById(userId, bookingId));
    }

    @GetMapping()
    public List<BookingWithItemAndBookerDto> getBookingByBookerId(@RequestHeader("X-Sharer-User-Id") int userId,
                                                                  @RequestParam(name = "state", required = false,
                                                                          defaultValue = "ALL") String state) {
        List<Booking> bookings = bookingService.getBookingByBookerId(userId, state);
        List<BookingWithItemAndBookerDto> bookingsDto = new ArrayList<>();
        bookings.forEach(booking -> bookingsDto.add(BookingMapper.toBookingWithItemAndBookerDto(booking)));
        return bookingsDto;

    }

    @GetMapping("/owner")
    public List<BookingWithItemAndBookerDto> getBookingByOwnerId(@RequestHeader("X-Sharer-User-Id") int userId,
                                                                 @RequestParam(name = "state", required = false,
                                                                         defaultValue = "ALL") String state) {
        List<Booking> bookings = bookingService.getBookingByOwnerId(userId, state);
        List<BookingWithItemAndBookerDto> bookingsDto = new ArrayList<>();
        bookings.forEach(booking -> bookingsDto.add(BookingMapper.toBookingWithItemAndBookerDto(booking)));
        return bookingsDto;
    }
}
