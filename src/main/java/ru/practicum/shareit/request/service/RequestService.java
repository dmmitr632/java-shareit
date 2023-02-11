package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestService {
    Request addRequest(RequestDto requestDto, int requesterId);

    List<Request> getRequestsByUserId(int userId, int requestId);

    Page<Request> getRequestsOfOtherUsers(int userId, int from, int size);

    Request getRequestById(int requestId);
}
