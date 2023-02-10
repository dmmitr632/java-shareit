package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

public class RequestMapper {
    public static Request toRequest(RequestDto requestDto, User requester) {
        return new Request(requestDto.getId(), requestDto.getDescription(), requester);
    }

    public static RequestDto toRequestDto(Request request) {
        return new RequestDto(request.getId(), request.getDescription(),
                request.getRequester().getId());
    }
}
