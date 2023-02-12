package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestService {
    Request addRequest(RequestDto requestDto, int requesterId);

    List<Request> getRequestsByUserId(int userId);

    List<Request> getRequestsOfOtherUsers(int userId, Integer from, Integer size);

    Request getRequestById(int userId, int requestId);
}
