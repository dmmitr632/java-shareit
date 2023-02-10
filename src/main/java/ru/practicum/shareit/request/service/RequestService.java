package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;

public interface RequestService {
    Request addRequest(RequestDto requestDto);
}
