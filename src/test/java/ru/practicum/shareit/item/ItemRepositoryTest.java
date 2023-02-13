package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemQueueInfo;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ItemRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ItemRepository itemRepository;
    User user1 = new User(null, "item owner", "user1@google.com");
    User user2 = new User(null, "item requester", "user2@google.com");
    Request request = new Request(null, "i need item", user2, LocalDateTime.now(), null);
    Item item = new Item(null, "Item", "description", true, user1, request);
    Comment comment = new Comment(null, "comment", item, user2, LocalDateTime.now().plusWeeks(2));

    LocalDateTime currentTime = LocalDateTime.now();
    LocalDateTime nextWeek = LocalDateTime.now().plusWeeks(1);
    Pageable pageable = PageRequest.of(0, 100);

    @BeforeEach
    void beforeEachTest() {
        testEntityManager.persist(user1);
        testEntityManager.persist(user2);
        testEntityManager.persist(request);
        testEntityManager.persist(item);
        testEntityManager.persist(comment);
    }

    @Test
    void search() {
        itemRepository.save(item);
        Page<Item> items = itemRepository.search("escrip", pageable);
        assertThat(items.stream().findFirst().get(), equalTo(item));
    }

    @Test
    void findAllByUserIdAndTime() {
        itemRepository.save(item);
        ItemQueueInfo itemQueueInfo = itemRepository.findAllByUserIdAndTime(1, LocalDateTime.now(), pageable)
                .stream()
                .collect(Collectors.toList())
                .stream()
                .findFirst()
                .get();
        ItemDto itemDto1 = ItemMapper.toItemDtoFromQueueAndComments(itemQueueInfo, new ArrayList<>());
        ItemDto itemDto2 = ItemMapper.toItemDto(item);
        assertThat(itemDto1, equalTo(itemDto2));

    }

    @Test
    void findByItemIdAndTime() {
        itemRepository.save(item);
        ItemQueueInfo itemQueueInfo = itemRepository.findByItemIdAndTime(1, LocalDateTime.now());
        ItemDto itemDto1 = ItemMapper.toItemDtoFromQueueAndComments(itemQueueInfo, new ArrayList<>());
        ItemDto itemDto2 = ItemMapper.toItemDto(item);
        assertThat(itemDto1, equalTo(itemDto2));
    }
}