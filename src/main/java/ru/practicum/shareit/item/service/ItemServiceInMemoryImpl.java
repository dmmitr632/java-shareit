//package ru.practicum.shareit.item.service;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Service;
//import ru.practicum.shareit.exception.NotFoundException;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.item.storage.ItemStorage;
//import ru.practicum.shareit.user.storage.UserStorage;
//
//import java.util.List;
//
//@Service
//public class ItemServiceInMemoryImpl implements ItemService {
//
//    private final ItemStorage itemStorage;
//    private final UserStorage userStorage;
//
//    public ItemServiceInMemoryImpl(@Qualifier("ItemStorageInMemory") ItemStorage itemStorage,
//                                   @Qualifier("UserStorageInMemory") UserStorage userStorage) {
//        this.itemStorage = itemStorage;
//        this.userStorage = userStorage;
//    }
//
//    @Override
//    public Item addItem(int userId, Item item) {
//
//        item.setOwner(userStorage.getUserById(userId));
//        return itemStorage.addItem(item, userId);
//
//
//    }
//
//    @Override
//    public Item editItem(int userId, int itemId, Item item) {
//        userStorage.getUserById(userId);
//        Item editedItem = itemStorage.getItemById(itemId);
//
//
//        if (editedItem.getOwner() == null && item.getOwner() != null)
//            throw new NotFoundException("неверный id");
//        else if (editedItem.getOwner() != null && userId != editedItem.getOwner().getId()) {
//            throw new NotFoundException("неверный id");
//        }
//
//        if (item.getId() == null) item.setId(editedItem.getId());
//
//        if (item.getName() == null) item.setName(editedItem.getName());
//
//        if (item.getDescription() == null) item.setDescription(editedItem.getDescription());
//
//        if (item.getAvailable() == null) item.setAvailable(editedItem.getAvailable());
//
//
//        item.setOwner(editedItem.getOwner());
//        return itemStorage.editItem(userId, itemId, item);
//
//
//    }
//
//    @Override
//    public Item getItemById(int id) {
//        return itemStorage.getItemById(id);
//
//
//    }
//
//    @Override
//    public List<Item> getAllItemsByUserId(int id) {
//        return itemStorage.getAllItemsByUserId(id);
//    }
//
//    @Override
//    public List<Item> getItemsByTextSearch(String text) {
//        return itemStorage.getItemsByTextSearch(text);
//    }
//}
