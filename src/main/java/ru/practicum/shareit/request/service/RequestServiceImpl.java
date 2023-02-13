package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<Request> getRequestsByUserId(int userId) {
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        return requestRepository.findAllByRequesterIdOrderByCreatedDesc(userId);
    }

    @Override
    public List<Request> getRequestsOfOtherUsers(int userId, Integer from, Integer size) {
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        if (size == null) {
            size = Integer.MAX_VALUE;
        }

        return requestRepository.findAllByRequesterIdNotOrderByCreatedDesc(
                        userId, Pageable.ofSize(from + size)).stream()
                .skip(from).collect(Collectors.toList());

    }

    @Override
    public Request getRequestById(int userId, int requestId) {
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        return requestRepository.findById(requestId).orElseThrow(NotFoundException::new);
    }

}
