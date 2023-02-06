package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Qualifier;
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
import ru.practicum.shareit.item.dto.ItemLastNextBooking;
import ru.practicum.shareit.item.dto.ItemLastNextBookingDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("ItemServiceDb")
public class ItemServiceImpl implements ItemService {
    public final ItemRepository itemRepository;
    public final UserRepository userRepository;
    public final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository,
                           BookingRepository bookingRepository,
                           CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Item addItem(int userId, ItemDto itemDto) {
        User owner = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Item item = ItemMapper.toItem(itemDto, owner);
        return itemRepository.save(item);
    }

    @Override
    public Item editItem(int userId, int itemId, ItemDto itemDto) {
        User owner = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Item item = ItemMapper.toItem(itemDto, owner);
        Item editedItem = itemRepository.findById(itemId).orElseThrow(NotFoundException::new);

        if (editedItem.getOwner() == null && item.getOwner() != null) throw new NotFoundException("неверный id");
        else if (editedItem.getOwner() != null && userId != editedItem.getOwner().getId()) {
            throw new NotFoundException("неверный id");
        }

        if (item.getId() == null) item.setId(editedItem.getId());
        if (item.getName() == null) item.setName(editedItem.getName());
        if (item.getDescription() == null) item.setDescription(editedItem.getDescription());

        if (item.getAvailable() == null) item.setAvailable(editedItem.getAvailable());
        item.setOwner(editedItem.getOwner());

        return itemRepository.save(item);

    }

    @Override
    public ItemLastNextBookingDto getItemById(int userId, int itemId) {
        itemRepository.findById(itemId).orElseThrow(NotFoundException::new);
        ItemLastNextBooking itemWithBooking = itemRepository.findByItemIdAndTime(itemId, LocalDateTime.now());
        List<Comment> comments = commentRepository.findAllByItem_id(itemId);

        if (itemWithBooking == null) {
            Item item = itemRepository.findById(itemId).orElseThrow(NotFoundException::new);
            return new ItemLastNextBookingDto(item.getId(), item.getName(),
                    item.getDescription(), item.getAvailable(), null, null, comments);
        }

        if (userId != itemWithBooking.getOwnerId()) {
            return new ItemLastNextBookingDto(itemWithBooking.getId(), itemWithBooking.getName(),
                    itemWithBooking.getDescription(), itemWithBooking.getAvailable(), null, null, comments);
        }
        return ItemMapper.toItemLastNextBookingDto(itemWithBooking, comments);
    }


    @Override
    public List<ItemLastNextBookingDto> getAllItemsByUserId(int ownerId) {
        List<ItemLastNextBooking> foundItems = itemRepository.findAllByUserIdAndTime(ownerId, LocalDateTime.now());
        List<ItemLastNextBookingDto> itemDtoList = new ArrayList<>();

        foundItems.forEach(item -> itemDtoList.add(ItemMapper.toItemLastNextBookingDto(item,
                commentRepository.findAllByItem_id(item.getId()))));
        return itemDtoList;
    }

    @Override
    public List<Item> getItemsByTextSearch(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.search(text);
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
        // comment.setCreated(LocalDateTime.now());
        comment.setCreated(createdTime);
        comment.setAuthor(author);
        commentRepository.save(comment);
        return CommentMapper.toCommentDto(comment);
    }


}
