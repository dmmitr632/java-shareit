package ru.practicum.shareit.request.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    public RequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Request addRequest(RequestDto requestDto, int requesterId) {
        User requester = userRepository.findById(requesterId).orElseThrow(NotFoundException::new);
        return requestRepository.save(RequestMapper.toRequestWithoutItems(requestDto, requester));

    }

    @Override
    public List<Request> getRequestsByOwnerId(int userId, int requestId) {
        return null;
    }

    @Override
    public List<Request> getRequestsOfOtherUsers(int userId, int from, int size) {
        return null;
    }

    @Override
    public Request getRequestById(int userId, int requestId) {
        return null;
    }


}
