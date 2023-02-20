package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/requests")
@Validated
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @Valid @RequestBody RequestRequestDto requestDto) {
        log.info("Add item request for user with id {}", userId);
        return requestClient.addRequest(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get all item requests for user with id {} ", userId);
        return requestClient.getRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequestsOfOtherUsers(@RequestHeader("X-Sharer-User-Id") long userId,
                                                          @PositiveOrZero @RequestParam(name = "from",
                                                                  required = false, defaultValue = "0") Integer from,
                                                          @Positive @RequestParam(name = "size", required =
                                                                  false, defaultValue = "100") Integer size) {
        log.info("Get all item requests excluding requests of user with id {}", userId);
        return requestClient.getRequestsOfOtherUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable Long requestId,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get request with id {}", requestId);
        return requestClient.getRequestById(requestId, userId);
    }
}