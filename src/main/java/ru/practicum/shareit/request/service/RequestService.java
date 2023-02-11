package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestService {
    Request addRequest(RequestDto requestDto, int requesterId);

    List<Request> getRequestsByOwnerId(int userId, int requestId);

    List<Request> getRequestsOfOtherUsers(int userId, int from, int size);

    Request getRequestById(int userId, int requestId);
}
