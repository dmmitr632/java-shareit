package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemQueueInfo;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Qualifier("ItemServiceDb")
public class ItemServiceImpl implements ItemService {
    public final ItemRepository itemRepository;
    public final UserRepository userRepository;
    public final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final RequestRepository requestRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository,
                           BookingRepository bookingRepository,
                           CommentRepository commentRepository, RequestRepository requestRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public ItemDto addItem(int userId, ItemShortDto itemShortDto) {
        User owner = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Request request = null;
        if (itemShortDto.getRequestId() != null) {
            request = requestRepository.findById(itemShortDto.getRequestId())
                    .orElseThrow(NotFoundException::new);
        }
        Item item = ItemMapper.toItem(itemShortDto, owner, request);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto editItem(int userId, int itemId, ItemShortDto itemShortDto) {
        User owner = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Request request = null;
        if (itemShortDto.getRequestId() != null) {
            request = requestRepository.findById(itemShortDto.getRequestId())
                    .orElseThrow(NotFoundException::new);
        }
        Item item = ItemMapper.toItem(itemShortDto, owner, request);
        Item editedItem = itemRepository.findById(itemId).orElseThrow(NotFoundException::new);

        if (editedItem.getOwner() == null && item.getOwner() != null) {
            throw new NotFoundException("неверный id");
        } else if (editedItem.getOwner() != null && userId != editedItem.getOwner().getId()) {
            throw new NotFoundException("неверный id");
        }

        if (item.getId() == null) {
            item.setId(editedItem.getId());
        }
        if (item.getName() == null) {
            item.setName(editedItem.getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(editedItem.getDescription());
        }

        if (item.getAvailable() == null) {
            item.setAvailable(editedItem.getAvailable());
        }
        if (item.getRequest() == null) {
            item.setRequest(editedItem.getRequest());
        }
        item.setOwner(editedItem.getOwner());

        return ItemMapper.toItemDto(itemRepository.save(item));

    }

    @Override
    public ItemDto getItemById(int userId, int itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(NotFoundException::new);
        ItemQueueInfo itemWithBooking = itemRepository.findByItemIdAndTime(itemId, LocalDateTime.now());
        List<Comment> comments = commentRepository.findAllByItem_id(itemId);
        List<CommentDto> commentsDto = new ArrayList<>();
        comments.forEach(comment -> commentsDto.add(CommentMapper.toCommentDto(comment)));

        if (itemWithBooking == null) {
            return new ItemDto(item.getId(), item.getName(),
                    item.getDescription(), item.getAvailable(), null, null, null, commentsDto);
        }

        if (userId != itemWithBooking.getOwnerId()) {
            return new ItemDto(itemWithBooking.getId(), itemWithBooking.getName(),
                    itemWithBooking.getDescription(), itemWithBooking.getAvailable(), null, null, null,
                    commentsDto);
        }
        return ItemMapper.toItemDtoFromQueueAndCommentsDto(itemWithBooking, commentsDto);

    }

    @Override
    public List<ItemDto> getAllItemsByUserId(int ownerId, Integer from, Integer size) {
        if (size == null) {
            size = Integer.MAX_VALUE;
        }
        Pageable pageable = PageRequest.of(from, size);

        List<ItemQueueInfo> foundItems = (itemRepository.findAllByUserIdAndTime(ownerId,
                LocalDateTime.now(),
                pageable)).stream().collect(Collectors.toList());
        List<ItemDto> itemDtoList = new ArrayList<>();

        foundItems.forEach(item -> itemDtoList.add(ItemMapper.toItemDtoFromQueueAndComments(item,
                commentRepository.findAllByItem_id(item.getId()))));
        return itemDtoList;
    }

    @Override
    public List<ItemDto> getItemsByTextSearch(String text, Integer from, Integer size) {
        if (size == null) {
            size = Integer.MAX_VALUE;
        }
        Pageable pageable = PageRequest.of(from, size);
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.search(text, pageable)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(int userId, int itemId, CommentDto commentDto, LocalDateTime createdTime) {
        Item item = itemRepository.findById(itemId).orElseThrow(NotFoundException::new);
        User author = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Booking booking = bookingRepository.findFirstByBooker_IdAndItem_Id(userId, itemId)
                .orElseThrow(ValidationException::new);
        if (createdTime.isBefore(booking.getEnd())) {
            throw new ValidationException("Lease is not over");
        }
        Comment comment = CommentMapper.toComment(commentDto, item, author);
        comment.setCreated(createdTime);
        commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment);
    }

}
