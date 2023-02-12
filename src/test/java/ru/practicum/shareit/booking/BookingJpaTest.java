package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;


@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BookingJpaTest {
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private BookingRepository bookingRepository;

    User user1 = new User(null, "User1", "user1@google.com");
    User user2 = new User(null, "User2", "user2@google.com");
    Item item = new Item(null, "Item", "item_description", true, user2, null);
    Booking booking = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusWeeks(1), item, user1,
            BookingStatus.APPROVED);

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    Pageable pageable = PageRequest.of(0, 100);


    @BeforeEach
    void beforeEachTest() {
        testEntityManager.persist(user1);
        testEntityManager.persist(user2);
        testEntityManager.persist(item);
        testEntityManager.persist(booking);
    }

    @Test
    void findByBooker() {
        Booking bookingSaved = Booking.builder().id(1).start(LocalDateTime.parse(booking.getStart().format(dateFormatter), dateFormatter)).end(LocalDateTime.parse(booking.getEnd().format(dateFormatter), dateFormatter)).build();
        Booking booking = bookingRepository.findFirstByBooker_IdAndItem_Id(1, 1).get();
        Assertions.assertEquals(bookingSaved, booking);
    }

    @Test
    void findByOwner() {
        Booking bookingSaved = Booking.builder().id(1).start(LocalDateTime.parse(booking.getStart().format(dateFormatter), dateFormatter)).end(LocalDateTime.parse(booking.getEnd().format(dateFormatter), dateFormatter)).build();
        Booking booking = bookingRepository.findAllByOwnerId(2, pageable).stream().collect(Collectors.toList()).get(0);
        Assertions.assertEquals(bookingSaved, booking);
    }

}