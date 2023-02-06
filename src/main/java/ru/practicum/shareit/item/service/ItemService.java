package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemLastNextBookingDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemService {
    Item addItem(int userId, ItemDto itemDto);

    Item editItem(int userId, int itemId, ItemDto itemDto);

    ItemLastNextBookingDto getItemById(int userId, int itemId);

    List<ItemLastNextBookingDto> getAllItemsByUserId(int id);

    List<Item> getItemsByTextSearch(String text);

}