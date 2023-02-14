package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.Constants.MAX_INTEGER_AS_STRING;

@WebMvcTest(controllers = RequestController.class)
public class RequestControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RequestService requestService;

    @Autowired
    private MockMvc mvc;
    public RequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = RequestDto.builder().id(1).description("description").build();
    }

    @Test
    void addRequest() throws Exception {
        when(requestService.addRequest(any(), anyInt())).thenReturn(requestDto);
        mvc.perform(post("/requests").content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(requestDto)));
        Mockito.verify(requestService, Mockito.times(1)).addRequest(requestDto, 1);
    }

    @Test
    void getRequestsByOwnerId() throws Exception {
        when(requestService.getRequestsByUserId(anyInt())).thenReturn(List.of(requestDto));
        mvc.perform(get("/requests").characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(requestDto))));
        Mockito.verify(requestService, Mockito.times(1)).getRequestsByUserId(1);
    }

    @Test
    void getAllRequestsCreatedByOtherUsers() throws Exception {
        when(requestService.getRequestsOfOtherUsers(anyInt(), anyInt(), anyInt())).thenReturn(
                List.of(requestDto));
        mvc.perform(get("/requests/all").characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(requestDto))));
        Mockito.verify(requestService, Mockito.times(1))
                .getRequestsOfOtherUsers(2, 0, Integer.valueOf(MAX_INTEGER_AS_STRING));
    }

    @Test
    void getRequest() throws Exception {
        when(requestService.getRequestById(anyInt(), anyInt())).thenReturn(requestDto);
        mvc.perform(get("/requests/1").characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(requestDto)));
        Mockito.verify(requestService, Mockito.times(1)).getRequestById(1, 1);
    }
}
