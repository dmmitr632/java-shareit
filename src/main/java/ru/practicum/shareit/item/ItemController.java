package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemLastNextBookingDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") int userId, @Valid @RequestBody ItemDto itemDto) {
        return ItemMapper.toItemDto(itemService.addItem(userId, itemDto));
    }

    @PatchMapping("{itemId}")
    public ItemDto editItem(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int itemId,
                            @RequestBody ItemDto itemDto) {
        return ItemMapper.toItemDto(itemService.editItem(userId, itemId, itemDto));
    }

    @GetMapping("{itemId}")
    public ItemLastNextBookingDto getItemById(@PathVariable int itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemLastNextBookingDto> getAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") int userId) {
        //List<Item> itemList = itemService.getAllItemsByUserId(userId);
        //List<ItemWithNextAndLastBookingsDto> itemDtoList = new ArrayList<>();
        //itemList.forEach(item -> itemDtoList.add(ItemMapper.toItemWithNextAndLastBookingsDto(item)));

        return itemService.getAllItemsByUserId(userId);
    }

    @GetMapping("search")
    public List<ItemDto> getItemsByTextSearch(@RequestParam String text) {
        List<Item> itemList = itemService.getItemsByTextSearch(text);
        List<ItemDto> itemDtoList = new ArrayList<>();
        itemList.forEach(item -> itemDtoList.add(ItemMapper.toItemDto(item)));
        return itemDtoList;
    }

//    @GetMapping("/{itemId}")
//    public ItemWithNextAndLastBookingsDto getItemDto(@RequestHeader("X-Sharer-User-Id") int userId,
//                                                     @PathVariable int itemId) {
//        return itemService.getItemWithNextAndLastBookings(userId, itemId);
//    }


}






