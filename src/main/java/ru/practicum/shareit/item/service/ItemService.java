package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;


@Service
public class ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemService(@Qualifier("ItemStorageInMemory") ItemStorage itemStorage,
                       @Qualifier("UserStorageInMemory") UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    public Item addItem(int userId, Item item) {
        if (userStorage.getUserById(userId) != null)
            return itemStorage.addItem(item, userId);
        else throw new NotFoundException();

    }

    public Item editItem(int userId, int itemId, ItemDto itemDto) {
        if (itemStorage.getItemById(itemId) != null)
            return itemStorage.editItem(ItemMapper.toItem(itemDto, userId), userId);
        else throw new NotFoundException();

    }

    public Item getItemById(int id) {
        return itemStorage.getItemById(id);

    }

    public List<Item> getAllItemsByUserId(int id) {
        return itemStorage.getAllItemsByUserId(id);
    }


    public List<Item> getItemsByTextSearch(String text) {
        return itemStorage.getItemsByTextSearch(text);
    }
}