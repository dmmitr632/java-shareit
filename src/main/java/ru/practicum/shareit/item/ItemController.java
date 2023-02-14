package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.Constants.MAX_INTEGER_AS_STRING;

@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") int userId,
                           @Valid @RequestBody ItemShortDto itemShortDto) {
        return itemService.addItem(userId, itemShortDto);
    }

    @PatchMapping("{itemId}")
    public ItemDto editItem(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int itemId,
                            @RequestBody ItemShortDto itemShortDto) {
        return itemService.editItem(userId, itemId, itemShortDto);
    }

    @GetMapping("{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int itemId) {
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") int userId,
                                             @RequestParam(defaultValue = "0", required = false) Integer from,
                                             @RequestParam(defaultValue = MAX_INTEGER_AS_STRING,
                                                     required = false) Integer size) {
        return itemService.getAllItemsByUserId(userId, from, size);
    }

    @GetMapping("search")
    public List<ItemDto> getItemsByTextSearch(@RequestParam String text,
                                              @RequestParam(defaultValue = "0", required =
                                                      false) Integer from,
                                              @RequestParam(defaultValue = MAX_INTEGER_AS_STRING,
                                                      required = false) Integer size) {
        return itemService.getItemsByTextSearch(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                 @PathVariable int itemId,
                                 @RequestBody @Valid CommentDto commentDto) {
        LocalDateTime commentCreatedTime = LocalDateTime.now();
        return itemService.addComment(userId, itemId, commentDto, commentCreatedTime);
    }
}
