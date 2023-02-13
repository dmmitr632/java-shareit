package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item addItem(Item item, int id);

    Item editItem(int userId, int itemId, Item item);

    Item getItemById(int id);

    List<Item> getAllItemsByUserId(int id);

    List<Item> getItemsByTextSearch(String text);

}
