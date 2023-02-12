package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
        return RequestMapper.toRequestDto(requestService.addRequest(requestDto, userId));
    }

    @GetMapping()
    public List<RequestDto> getRequestsByOwnerId(@RequestHeader("X-Sharer-User-Id") int ownerId) {
        List<Request> requests = requestService.getRequestsByUserId(ownerId);
        return requests.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @GetMapping("all")
    public List<RequestDto> getAllRequestsCreatedByOtherUsers(@RequestHeader("X-Sharer-User-Id") int userId,
                                                              @RequestParam(defaultValue = "0", required = false) Integer from,
                                                              @RequestParam(required = false) Integer size) {
        List<Request> requests = requestService.getRequestsOfOtherUsers(userId, from, size);

        return (requests.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList()));
    }

    @GetMapping("{requestId}")
    public RequestDto getRequest(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int requestId) {
        return RequestMapper.toRequestDto(requestService.getRequestById(userId, requestId));
    }
}
