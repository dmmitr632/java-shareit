package ru.practicum.shareit.itemrequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.itemrequest.dto.ItemRequestDto;
import ru.practicum.shareit.itemrequest.mapper.ItemRequestMapper;
import ru.practicum.shareit.itemrequest.service.ItemRequestService;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ItemRequestDto addItemRequest(ItemRequestDto itemRequestDto) {
        return ItemRequestMapper.toItemRequestDto(itemRequestService.addItemRequest(itemRequestDto));
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