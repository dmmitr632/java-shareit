package ru.practicum.shareit.request.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
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
    public RequestDto addRequest(RequestDto requestDto, int requesterId) {
        User requester = userRepository.findById(requesterId).orElseThrow(NotFoundException::new);
        return RequestMapper.toRequestDto(
                requestRepository.save(RequestMapper.toRequestWithoutItems(requestDto,
                        requester)));
    }

    @Override
    public List<RequestDto> getRequestsByUserId(int userId) {
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        return requestRepository.findAllByRequesterIdOrderByCreatedDesc(userId)
                .stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestDto> getRequestsOfOtherUsers(int userId, Integer from, Integer size) {
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Pageable pageable = PageRequest.of(from, size);
        return requestRepository.findAllByRequesterIdNotOrderByCreatedDesc(
                        userId, pageable).stream()
                .skip(from).map(RequestMapper::toRequestDto).collect(Collectors.toList());

    }

    @Override
    public RequestDto getRequestById(int userId, int requestId) {
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        return RequestMapper.toRequestDto(
                requestRepository.findById(requestId).orElseThrow(NotFoundException::new));
    }

}
