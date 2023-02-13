package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;


public interface ItemService {
    Item addItem(int userId, ItemShortDto itemShortDto);

    Item editItem(int userId, int itemId, ItemShortDto itemShortDto);

    ItemDto getItemById(int userId, int itemId);

    List<ItemDto> getAllItemsByUserId(int ownerId, Integer from, Integer size);

    List<Item> getItemsByTextSearch(String text, Integer from, Integer size);

    CommentDto addComment(int userId, int itemId, CommentDto commentDto, LocalDateTime createdTime);
}