package ru.practicum.shareit.itemrequest.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.itemrequest.dto.ItemRequestDto;
import ru.practicum.shareit.itemrequest.mapper.ItemRequestMapper;
import ru.practicum.shareit.itemrequest.model.ItemRequest;
import ru.practicum.shareit.itemrequest.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, UserRepository userRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemRequest addItemRequest(ItemRequestDto itemRequestDto){
        User requester = userRepository.findById(itemRequestDto.getRequesterId()).orElseThrow(NotFoundException::new);
        return itemRequestRepository.save(ItemRequestMapper.toItemRequest(itemRequestDto, requester));
    }
}
