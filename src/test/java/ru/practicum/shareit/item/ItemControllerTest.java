package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest(controllers =ItemController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTest {
    @Autowired
    ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .build();

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private ItemDto itemDto;
    CommentDto commentDto;

    @BeforeEach
    void setUp() {
        itemDto = ItemDto.builder().id(1).name("item").description("description").available(true).build();
        commentDto = CommentDto.builder().id(1).text("comment").created(LocalDateTime.now()).build();
    }

    @Test
    void addItem() throws Exception {
        when(itemService.addItem(anyInt(), any())).thenReturn(itemDto);
        mvc.perform(post("/items").content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)));
    }

    @Test
    void editItem() throws Exception {
        when(itemService.editItem(anyInt(), anyInt(), any())).thenReturn(itemDto);
        mvc.perform(patch("/items/1").content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)));
    }

    @Test
    void getItemById() throws Exception {
        when(itemService.getItemById(anyInt(), anyInt())).thenReturn(itemDto);
        mvc.perform(get("/items/1").header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)));
    }

    @Test
    void getAllItemsByUserId() throws Exception {
        when(itemService.getAllItemsByUserId(anyInt(), anyInt(), anyInt())).thenReturn(List.of(itemDto));
        mvc.perform(get("/items").characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(itemDto))));
    }

    @Test
    void getItemsByTextSearch() throws Exception {
        when(itemService.getItemsByTextSearch(anyString(), anyInt(), anyInt())).thenReturn(List.of(itemDto));
        mvc.perform(get("/items/search?text=name").header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(itemDto))));
    }

    @Test
    void addComment() throws Exception {
        when(itemService.addComment(anyInt(), anyInt(), any())).thenReturn(
                commentDto);
        mvc.perform(post("/items/1/comment").header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(commentDto)));
    }

}
