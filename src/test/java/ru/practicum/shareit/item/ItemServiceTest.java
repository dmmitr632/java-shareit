package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest(controllers =ItemController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ItemServiceTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private ItemDto itemDto;

    @BeforeEach
    void init() {
        UserDto userDto = UserDto.builder().id(2).name("user 1").email("user@user.ru").build();
        itemDto = ItemDto.builder().id(1).name("item").description("description").available(true).build();
        CommentDto commentDto = CommentDto.builder()
                .id(1)
                .text("comment")
                .created(LocalDateTime.now())
                .build();
    }



    @Test
    void addItem() throws Exception {
        when(itemService.addItem(anyInt(), any())).thenReturn(itemDto);
        mvc.perform(post("/items").content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto)));
    }

    @Test
    void editItem() throws Exception {
        when(itemService.editItem(anyInt(), anyInt(), any())).thenReturn(itemDto);
        mvc.perform(patch("/items/1").content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto)));
    }

    @Test
    void getItemById() throws Exception {
        when(itemService.getItemById(anyInt(), anyInt())).thenReturn(itemDto);
        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto)));
    }

}
