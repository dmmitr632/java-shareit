package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemService {
    Item addItem(int userId, Item item);

    Item editItem(int userId, int itemId, Item item);

    Item getItemById(int id);

    List<Item> getAllItemsByUserId(int id);

    List<Item> getItemsByTextSearch(String text);
}