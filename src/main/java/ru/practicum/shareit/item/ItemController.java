package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") int userId, @Valid @RequestBody Item item) {
        return ItemMapper.toItemDto(itemService.addItem(userId, item));
    }

    @PatchMapping("{itemId}")
    public ItemDto editItem(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int itemId,
                         @RequestBody ItemDto itemDto) {
        return ItemMapper.toItemDto(itemService.editItem(userId, itemId, itemDto));
    }

    @GetMapping("{itemId}")
    public ItemDto getItemById(@PathVariable int itemId) {
        return ItemMapper.toItemDto(itemService.getItemById(itemId));
    }

    @GetMapping
    public List<ItemDto> getAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") int userId) {
        List<Item> itemList = itemService.getAllItemsByUserId(userId);
        List<ItemDto> itemDtoList = new ArrayList<>();
        itemList.forEach(item -> itemDtoList.add(ItemMapper.toItemDto(item)));
        return itemDtoList;
    }

    @GetMapping("search")
    public List<ItemDto> getItemsByTextSearch(@RequestParam String text) {
        List<Item> itemList = itemService.getItemsByTextSearch(text);
        List<ItemDto> itemDtoList = new ArrayList<>();
        itemList.forEach(item -> itemDtoList.add(ItemMapper.toItemDto(item)));
        return itemDtoList;
    }
}
