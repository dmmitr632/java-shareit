package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemLastNextBookingDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.time.LocalDateTime;
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
    public ItemLastNextBookingDto getItemById(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int itemId) {
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemLastNextBookingDto> getAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") int userId,
                                                            @RequestParam(defaultValue = "0", required = false) Integer from,
                                                            @RequestParam(required = false) Integer size) {
        return itemService.getAllItemsByUserId(userId, from, size);
    }

    @GetMapping("search")
    public List<ItemDto> getItemsByTextSearch(@RequestParam String text, @RequestParam(defaultValue = "0", required =
            false) Integer from,
                                              @RequestParam(required = false) Integer size) {
        List<Item> itemList = itemService.getItemsByTextSearch(text, from, size);
        List<ItemDto> itemDtoList = new ArrayList<>();
        itemList.forEach(item -> itemDtoList.add(ItemMapper.toItemDto(item)));
        return itemDtoList;
    }


    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                 @PathVariable int itemId,
                                 @RequestBody @Valid CommentDto commentDto) {
        LocalDateTime commentCreatedTime = LocalDateTime.now();
        return itemService.addComment(userId, itemId, commentDto, commentCreatedTime);
    }
}
