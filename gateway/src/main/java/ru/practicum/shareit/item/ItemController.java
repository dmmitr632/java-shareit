package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestBody @Valid ItemRequestDto requestDto) {
        log.info("Add item, owned by {}", userId);
        return itemClient.addItem(userId, requestDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> editItem(@PathVariable Long id,
                                           @RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestBody ItemRequestDto requestDto) {
        log.info("Edit item {}", id);
        return itemClient.editItem(id, userId, requestDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@PathVariable Long id,
                                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get item with id {}", id);
        return itemClient.getItemById(id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @PositiveOrZero @RequestParam(name = "from",
                                                              required = false, defaultValue = "0") Integer from,
                                                      @Positive @RequestParam(name = "size", required =
                                                              false, defaultValue = "100") Integer size) {
        log.info("Get all items of user {}", userId);
        return itemClient.getAllItemsByUserId(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsByTextSearch(@RequestParam String text,
                                                       @PositiveOrZero @RequestParam(name = "from",
                                                               required = false, defaultValue = "0") Integer from,
                                                       @Positive @RequestParam(name = "size", required =
                                                               false, defaultValue = "100") Integer size) {
        log.info("Get items by text search {}", text);
        return itemClient.getItemsByTextSearch(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody @Valid CommentRequestDto commentDto) {
        log.info("Add comment to item {}", itemId);
        return itemClient.addComment(userId, itemId, commentDto);
    }
}