package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

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
        //return RequestMapper.toRequestDto(requestService.getRequestsByOwnerId(requestDto));
        return null;
    }

    @GetMapping("all")
    public List<RequestDto> getAllRequestsCreatedByOtherUsers(@RequestHeader("X-Sharer-User-Id") int userId,
                                                              @RequestParam(defaultValue = "0") int from,
                                                              @RequestParam(defaultValue = "50") int size) {
        //return RequestMapper.toRequestDto(requestService.getRequestsOfOtherUsers(userId, from, size));
        return null;
    }


    @GetMapping("{requestId}")
    public RequestDto getRequest(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int requestId) {
        //return requestService.getRequestById(userId, requestId);
        return null;
    }


}
//POST /requests — добавить новый запрос вещи.
// Основная часть запроса — текст запроса, где пользователь описывает, какая именно вещь ему нужна.
//GET /requests — получить список своих запросов вместе с данными об ответах на них.
// Для каждого запроса должны указываться описание, дата и время создания и список ответов в формате:
// id вещи, название, id владельца. Так в дальнейшем, используя указанные id вещей,
// можно будет получить подробную информацию о каждой вещи.
// Запросы должны возвращаться в отсортированном порядке от более новых к более старым.
//GET /requests/all?from={from}&size={size} — получить список запросов, созданных другими пользователями.
// С помощью этого эндпоинта пользователи смогут просматривать существующие запросы, на которые они могли бы ответить.
// Запросы сортируются по дате создания: от более новых к более старым.
// Результаты должны возвращаться постранично.
// Для этого нужно передать два параметра: from — индекс первого элемента, начиная с 0,
// и size — количество элементов для отображения.

//GET /requests/{requestId} — получить данные об одном конкретном запросе
// вместе с данными об ответах на него в том же формате,
// что и в эндпоинте GET /requests. Посмотреть данные об отдельном запросе может любой пользователь.