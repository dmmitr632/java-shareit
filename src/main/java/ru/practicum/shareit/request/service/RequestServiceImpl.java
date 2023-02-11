package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public List<Request> getRequestsByUserId(int userId, int requestId) {
        return requestRepository.findAllByRequesterIdOrderByCreatedDesc(userId);
    }

    @Override
    public Page<Request> getRequestsOfOtherUsers(int userId, int from, int size) {
        Pageable pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "created"));
        return requestRepository.findAllByRequesterIdNotOrderByCreatedDesc(userId, pageable);
    }

    @Override
    public Request getRequestById(int requestId) {
        return requestRepository.findById(requestId).orElseThrow(NotFoundException::new);
    }


}
