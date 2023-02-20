package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue =
													  "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                         @RequestParam(name = "state",
                                                                 defaultValue = "all") String stateString,
                                                         @PositiveOrZero @RequestParam(name = "from",
                                                                 required = false, defaultValue = "0")
                                                         Integer from,
                                                         @Positive @RequestParam(name = "size", required =
                                                                 false,
                                                                 defaultValue = "100")
                                                         Integer size) {
        BookingState state = BookingState.from(stateString)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateString));
        log.info("Get booking owner with userId={}, state {}, from={}, size={}",  userId, stateString, from,
                size);
        return bookingClient.getBookingOwner(userId, state, from, size);
    }




    @PatchMapping("{bookingId}")
	public ResponseEntity<Object> approveOrReject(@RequestParam boolean approved,
											   @RequestHeader("X-Sharer-User-Id") long userId,
											   @PathVariable long bookingId) {
		return bookingClient.approveOrReject(userId, bookingId, approved);
	}
}
