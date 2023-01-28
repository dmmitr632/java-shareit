package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
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

    public Item addItem(int id, Item item) {
        return itemStorage.addItem(id, item);

    }

    public Item editItem(int userId, int itemId, ItemDto itemDto) {
        Item item = itemStorage.getItemById(itemId);
        return itemStorage.editItem(userId, ItemMapper.toItem(itemDto, userId)); //Доделать toItem


    }

    public ItemDto getItemById(int id) {
        Item item = itemStorage.getItemById(id);
        return ItemMapper.toItemDto(item);
    }

    public List<Item> getAllItemsByUserId(int id) {
        return itemStorage.getAllItemsByUserId(id);
    }


    public List<ItemDto> getItemsByTextSearch(String text) {
        return null;
    }
}