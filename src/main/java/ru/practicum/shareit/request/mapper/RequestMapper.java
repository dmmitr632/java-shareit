package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestMapper {
    public static Request toRequest(RequestDto requestDto, User requester, User owner, Set<Item> items) {
        return new Request(requestDto.getId(), requestDto.getDescription(), requester,
                requestDto.getCreated(),
                items);

    }

    public static Request toRequestWithoutItems(RequestDto requestDto, User requester) {
        return new Request(requestDto.getId(), requestDto.getDescription(), requester, requestDto.getCreated(),
                new HashSet<>());
    }


    public static RequestDto toRequestDto(Request request) {
        Set<ItemDto> itemsDto = request.getItems().stream().map(ItemMapper::toItemDto).collect(Collectors.toSet());

        return new RequestDto(request.getId(), request.getDescription(), request.getCreated(), itemsDto);
    }


}
