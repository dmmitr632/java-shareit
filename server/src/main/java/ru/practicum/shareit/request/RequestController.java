package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.Constants.MAX_INTEGER_AS_STRING;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
public class RequestController {
    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public RequestDto addRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                 @Valid @RequestBody RequestDto requestDto) {
        return requestService.addRequest(requestDto, userId);
    }

    @GetMapping()
    public List<RequestDto> getRequestsByOwnerId(@RequestHeader("X-Sharer-User-Id") int ownerId) {
        return requestService.getRequestsByUserId(ownerId);
    }

    @GetMapping("all")
    public List<RequestDto> getAllRequestsCreatedByOtherUsers(@RequestHeader("X-Sharer-User-Id") int userId,
                                                              @RequestParam(defaultValue = "0", required =
                                                                      false) Integer from,
                                                              @RequestParam(defaultValue =
                                                                      MAX_INTEGER_AS_STRING,
                                                                      required = false) Integer size) {
        return requestService.getRequestsOfOtherUsers(userId, from, size);
    }

    @GetMapping("{requestId}")
    public RequestDto getRequest(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int requestId) {
        return requestService.getRequestById(userId, requestId);
    }
}
