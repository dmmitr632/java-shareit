package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
@Slf4j
@Component
@Qualifier("ItemStorageInMemory")
public class ItemStorageInMemory implements ItemStorage {


    private List<Item> items = new ArrayList<>();
    private int id = 0;


    @Override
    public Item addItem(int userId, Item item) {
        id++;
        item.setId(id);
        item.setOwner(userId);
        items.add(item);
        return item;
    }

    @Override
    public Item editItem(int userId, Item item) {
        items.remove(item.getId());
        items.add(item);
        return item;
    }

    @Override
    public Item getItemById(int itemId) {
        for (Item item : items) {
            if (item.getId() == itemId) return item;
        }
        return null;
    }

    @Override
    public List<Item> getAllItemsByUserId(int userId) {
        List<Item> itemsByUserId = new ArrayList<>();
        for (Item item : items) {
            if (item.getOwner() == userId) {
                itemsByUserId.add(item);
            }
        }
        return itemsByUserId;
    }

    @Override
    public List<Item> getItemsByTextSearch(String text) {
        return null;
    }
}