package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@Qualifier("ItemServiceDb")
public class ItemServiceImpl implements ItemService {
    public final ItemRepository itemRepository;
    public final UserRepository userRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Item addItem(int userId, ItemDto itemDto) {
        User owner = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Item item = ItemMapper.toItem(itemDto, owner);
        return itemRepository.save(item);
    }

    @Override
    public Item editItem(int userId, int itemId, ItemDto itemDto) {
        User owner = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Item item = ItemMapper.toItem(itemDto, owner);
        Item editedItem = itemRepository.findById(itemId).orElseThrow(NotFoundException::new);

        if (editedItem.getOwner() == null && item.getOwner() != null) throw new NotFoundException("неверный id");
        else if (editedItem.getOwner() != null && userId != editedItem.getOwner().getId()) {
            throw new NotFoundException("неверный id");
        }

        if (item.getId() == null) item.setId(editedItem.getId());
        if (item.getName() == null) item.setName(editedItem.getName());
        if (item.getDescription() == null) item.setDescription(editedItem.getDescription());




         if (item.getAvailable() == null) item.setAvailable(editedItem.getAvailable());
        //item.setOwner(editedItem.getOwner());

        return itemRepository.save(item);

    }

    @Override
    public Item getItemById(int id) {
        return itemRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public List<Item> getAllItemsByUserId(int id) {
        return itemRepository.findByOwnerId(id);
    }

    @Override
    public List<Item> getItemsByTextSearch(String text) {
        return itemRepository.search(text);
    }
}
