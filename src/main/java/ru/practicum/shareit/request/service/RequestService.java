package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto addRequest(RequestDto requestDto, int requesterId);

    List<RequestDto> getRequestsByUserId(int userId);

    List<RequestDto> getRequestsOfOtherUsers(int userId, Integer from, Integer size);

    RequestDto getRequestById(int userId, int requestId);
}
