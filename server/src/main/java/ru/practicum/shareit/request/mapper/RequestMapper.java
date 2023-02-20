package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestMapper {

    public static Request toRequestWithoutItems(RequestDto requestDto, User requester) {
        if (requestDto.getCreated() != null) {
            return new Request(requestDto.getId(), requestDto.getDescription(), requester,
                    requestDto.getCreated(),
                    new HashSet<>());
        } else {
            return new Request(requestDto.getId(), requestDto.getDescription(), requester,
                    LocalDateTime.now(),
                    new HashSet<>());
        }
    }

    public static RequestDto toRequestDto(Request request) {
        Set<ItemShortDto> itemsDto = request.getItems()
                .stream().map(ItemMapper::toItemShortDto).collect(Collectors.toSet());
        return new RequestDto(request.getId(), request.getDescription(), request.getCreated(), itemsDto);
    }

}
