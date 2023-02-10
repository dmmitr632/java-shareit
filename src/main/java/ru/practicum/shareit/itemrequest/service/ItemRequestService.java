package ru.practicum.shareit.itemrequest.service;

import ru.practicum.shareit.itemrequest.dto.ItemRequestDto;
import ru.practicum.shareit.itemrequest.model.ItemRequest;

public interface ItemRequestService {
    ItemRequest addItemRequest(ItemRequestDto itemRequestDto);
}
