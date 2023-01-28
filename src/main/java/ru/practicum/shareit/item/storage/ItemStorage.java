package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemStorage {
    Item addItem(int id, Item item);

    Item editItem(int id, Item item);

    Item getItemById(int id);

    List<Item> getAllItemsByUserId(int id);

    List<Item> getItemsByTextSearch(String text);

}
