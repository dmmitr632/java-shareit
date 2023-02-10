package ru.practicum.shareit.itemrequest.mapper;

import ru.practicum.shareit.itemrequest.dto.ItemRequestDto;
import ru.practicum.shareit.itemrequest.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

public class ItemRequestMapper {
    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User requester) {
        return new ItemRequest(itemRequestDto.getId(), itemRequestDto.getDescription(), requester);
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getId(), itemRequest.getDescription(),
                itemRequest.getRequester().getId());
    }
}
