package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
@Qualifier("ItemStorageInMemory")
public class ItemStorageInMemory implements ItemStorage {


    private final Map<Integer, Item> items = new HashMap<>();
    private int id = 0;


    @Override
    public Item addItem(Item item, int userId) {
        id++;
        item.setId(id);
        item.setOwner(userId);
        items.put(id, item);
        return item;
    }

    @Override
    public Item editItem(int userId, int itemId, Item item) {
        if (items.containsKey(itemId)) {
            items.put(item.getId(), item);
            return item;
        } else throw new NotFoundException();


    }

    @Override
    public Item getItemById(int itemId) {
        if (items.containsKey(itemId))
            return items.get(itemId);
        else throw new NotFoundException();
    }

    @Override
    public List<Item> getAllItemsByUserId(int id) {
        ArrayList<Item> foundItems = new ArrayList<>();

        for (Item item : items.values()) {
            if (item.getOwner() == id) foundItems.add(item);
        }
        return foundItems;
    }

    @Override
    public List<Item> getItemsByTextSearch(String text) {
        ArrayList<Item> foundItems = new ArrayList<>();
        if (text.isBlank()) {
            return foundItems;
        }
        for (Item item : items.values()) {
            if ((item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(text.toLowerCase())) && item.isAvailable()) {
                foundItems.add(item);
            }
        }

        return foundItems;
    }
}