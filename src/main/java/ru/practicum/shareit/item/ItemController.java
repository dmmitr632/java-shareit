package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.dto.ItemDto;
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
    public ItemShortDto addItem(@RequestHeader("X-Sharer-User-Id") int userId, @Valid @RequestBody ItemShortDto itemShortDto) {
        return ItemMapper.toItemShortDto(itemService.addItem(userId, itemShortDto));
    }

    @PatchMapping("{itemId}")
    public ItemShortDto editItem(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int itemId,
                                 @RequestBody ItemShortDto itemShortDto) {
        return ItemMapper.toItemShortDto(itemService.editItem(userId, itemId, itemShortDto));
    }

    @GetMapping("{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int itemId) {
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") int userId,
                                             @RequestParam(defaultValue = "0", required = false) Integer from,
                                             @RequestParam(required = false) Integer size) {
        return itemService.getAllItemsByUserId(userId, from, size);
    }

    @GetMapping("search")
    public List<ItemShortDto> getItemsByTextSearch(@RequestParam String text, @RequestParam(defaultValue = "0", required =
            false) Integer from,
                                                   @RequestParam(required = false) Integer size) {
        List<Item> itemList = itemService.getItemsByTextSearch(text, from, size);
        List<ItemShortDto> itemShortDtoList = new ArrayList<>();
        itemList.forEach(item -> itemShortDtoList.add(ItemMapper.toItemShortDto(item)));
        return itemShortDtoList;
    }


    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                 @PathVariable int itemId,
                                 @RequestBody @Valid CommentDto commentDto) {
        LocalDateTime commentCreatedTime = LocalDateTime.now();
        return itemService.addComment(userId, itemId, commentDto, commentCreatedTime);
    }
}
