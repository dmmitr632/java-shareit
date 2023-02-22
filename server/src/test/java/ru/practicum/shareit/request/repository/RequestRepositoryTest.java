package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RequestRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RequestRepository requestRepository;
    private final Pageable pageable = PageRequest.of(0, 100);
    private final LocalDateTime week = LocalDateTime.of(2023, 3, 14, 22, 0, 15);
    private final LocalDateTime nextWeek = week.plusWeeks(1);
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(User.builder().name("user1").email("user1@email.com").build());
        user2 = userRepository.save(User.builder().name("user2").email("user2@email.com").build());
    }

    @Test
    void findAllByRequesterIdOrderByCreatedDesc() {

        requestRepository.save(Request.builder()
                .description("description")
                .requester(user1)
                .created(LocalDateTime.now())
                .build());
        List<Request> items = requestRepository.findAllByRequesterIdOrderByCreatedDesc(user1.getId());
        assertThat(items.size(), equalTo(1));
    }

    @Test
    void findAllByRequesterIdNotOrderByCreatedDesc() {

        requestRepository.save(
                Request.builder().description("description").requester(user1).created(week).build());

        assertThat(requestRepository.findAllByRequesterIdNotOrderByCreatedDesc(user2.getId(), pageable)
                .stream()
                .count(), equalTo(1L));

        assertThat(requestRepository.findAllByRequesterIdNotOrderByCreatedDesc(user1.getId(), pageable)
                .stream()
                .count(), equalTo(0L));

    }
}
