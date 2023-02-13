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
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BookingRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;

    User booker = new User(null, "Booker", "booker@google.com");
    User owner = new User(null, "Owner", "owner@google.com");
    Item item = new Item(null, "Item", "item_description", true, owner, null);

    LocalDateTime currentTime = LocalDateTime.now();
    LocalDateTime nextWeek = LocalDateTime.now().plusWeeks(1);

    Booking booking = new Booking(null, currentTime, nextWeek, item, booker,
            BookingStatus.WAITING);

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    Pageable pageable = PageRequest.of(0, 100);

    @BeforeEach
    void beforeEachTest() {
        testEntityManager.persist(booker);
        testEntityManager.persist(owner);
        testEntityManager.persist(item);
        testEntityManager.persist(booking);
    }


    @Test
    void findFirstByBooker_IdAndItem_Id() {
        userRepository.save(booker);
        userRepository.save(owner);
        itemRepository.save(item);
        bookingRepository.save(booking);
        assertThat(bookingRepository.findFirstByBooker_IdAndItem_Id(booker.getId(), item.getId()).get(),
                equalTo(booking));
    }

    @Test
    void findAllByBookerIdOrderByStartDesc() {
        userRepository.save(booker);
        userRepository.save(owner);
        itemRepository.save(item);
        bookingRepository.save(booking);
        assertThat(bookingRepository.findAllByBookerIdOrderByStartDesc(booker.getId(), pageable)
                        .stream()
                        .findFirst()
                        .get(),
                equalTo(booking));
    }

    @Test
    void findAllByBookerIdAndEndIsBeforeOrderByStartDesc() {
        userRepository.save(booker);
        userRepository.save(owner);
        itemRepository.save(item);
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);
        assertThat(bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByStartDesc(booker.getId(),
                nextWeek.plusWeeks(1), pageable).stream().findFirst().get(), equalTo(booking));
    }

    @Test
    void findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc() {
        userRepository.save(booker);
        userRepository.save(owner);
        itemRepository.save(item);
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);
        assertThat(bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                        booker.getId(),
                        currentTime.plusWeeks(1), nextWeek.minusWeeks(1), pageable).stream().findFirst().get(),
                equalTo(booking));
    }

    @Test
    void findAllByBookerIdAndStartIsAfterOrderByStartDesc() {
        userRepository.save(booker);
        userRepository.save(owner);
        itemRepository.save(item);
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);
        assertThat(bookingRepository.findAllByBookerIdAndStartIsAfterOrderByStartDesc(booker.getId(),
                currentTime.minusWeeks(1), pageable).stream().findFirst().get(), equalTo(booking));
    }

    @Test
    void findAllByBookerIdAndStatusOrderByStartDesc() {
        userRepository.save(booker);
        userRepository.save(owner);
        itemRepository.save(item);
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);
        assertThat(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(booker.getId(),
                BookingStatus.APPROVED, pageable).stream().findFirst().get(), equalTo(booking));
    }

    @Test
    void findAllByOwnerId() {
        userRepository.save(booker);
        userRepository.save(owner);
        itemRepository.save(item);
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);
        assertThat(bookingRepository.findAllByOwnerId(owner.getId(), pageable).stream().findFirst().get(),
                equalTo(booking));
    }

    @Test
    void findAllByOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc() {
        userRepository.save(booker);
        userRepository.save(owner);
        itemRepository.save(item);
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);
        assertThat(
                bookingRepository.findAllByOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(owner.getId(),
                                currentTime.plusWeeks(1), nextWeek.minusWeeks(1), pageable)
                        .stream()
                        .findFirst()
                        .get(),
                equalTo(booking));
    }

    @Test
    void findAllByOwnerIdAndEndIsBeforeOrderByStartDesc() {
        userRepository.save(booker);
        userRepository.save(owner);
        itemRepository.save(item);
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);
        assertThat(bookingRepository.findAllByOwnerIdAndEndIsBeforeOrderByStartDesc(owner.getId(),
                nextWeek.plusWeeks(1), pageable).stream().findFirst().get(), equalTo(booking));
    }

    @Test
    void findAllByOwnerIdAndStartIsAfterOrderByStartDesc() {
        userRepository.save(booker);
        userRepository.save(owner);
        itemRepository.save(item);
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);
        assertThat(bookingRepository.findAllByOwnerIdAndStartIsAfterOrderByStartDesc(owner.getId(),
                currentTime.minusWeeks(1), pageable).stream().findFirst().get(), equalTo(booking));
    }

    @Test
    void findAllByOwnerIdAndStatusEqualsOrderByStartDesc() {
        userRepository.save(booker);
        userRepository.save(owner);
        itemRepository.save(item);
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);
        assertThat(bookingRepository.findAllByOwnerIdAndStatusEqualsOrderByStartDesc(owner.getId(),
                BookingStatus.APPROVED, pageable).stream().findFirst().get(), equalTo(booking));
    }

    @Test
    void findByBooker() {
        Booking bookingSaved = Booking.builder()
                .id(1)
                .start(LocalDateTime.parse(booking.getStart().format(dateFormatter), dateFormatter))
                .end(LocalDateTime.parse(booking.getEnd().format(dateFormatter), dateFormatter))
                .build();
        Booking booking2 = bookingRepository.findFirstByBooker_IdAndItem_Id(1, 1).get();
        Assertions.assertEquals(bookingSaved, booking2);
    }

    @Test
    void findByOwner() {
        Booking bookingSaved = Booking.builder().id(1)
                .start(LocalDateTime.parse(booking.getStart().format(dateFormatter), dateFormatter))
                .end(LocalDateTime.parse(booking.getEnd().format(dateFormatter), dateFormatter)).build();
        Booking booking2 = bookingRepository.findAllByOwnerId(2, pageable)
                .stream()
                .collect(Collectors.toList())
                .get(0);
        Assertions.assertEquals(bookingSaved, booking2);
    }


}