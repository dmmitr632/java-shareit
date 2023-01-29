package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
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

    public Item editItem(int userId, int itemId, Item item) {
        Item editedItem = getItemById(itemId);
        System.out.println(editedItem);
        System.out.println(userId);




        if (editedItem.getOwner() == null && item.getOwner() != null)
            throw new NotFoundException("неверный id");
        else if (editedItem.getOwner() != null && userId != editedItem.getOwner()) {
            throw new NotFoundException("неверный id");
        }

        if (item.getId() == null) item.setId(editedItem.getId());

        if (item.getName() == null) item.setName(editedItem.getName());

        if (item.getDescription() == null) item.setDescription(editedItem.getDescription());

        if (item.getAvailable() == null) item.setAvailable(editedItem.getAvailable());


        if (itemStorage.getItemById(itemId) != null && userStorage.getUserById(userId) != null)
            return itemStorage.editItem(userId, itemId, item);
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