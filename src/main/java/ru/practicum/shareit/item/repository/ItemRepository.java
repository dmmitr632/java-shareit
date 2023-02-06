package ru.practicum.shareit.item.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemLastNextBooking;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Qualifier("ItemRepositoryInDb")
public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query(" select i from Item i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))" +
            "and i.available = true")
    List<Item> search(String text);

    @Query(value = "SELECT i.item_id as id, i.name as name, i.description as description, i.available as available, " +
            "lastBooking.booking_id as lastBookingId, lastBooking.booker_id as lastBookingBookerId, " +
            "lastBooking.start_time as lastStart, lastBooking.end_time as lastEnd, " +
            "nextBooking.booking_id as nextBookingId, nextBooking.booker_id as nextBookingBookerId, " +
            "nextBooking.start_time as nextStart, nextBooking.end_time as nextEnd " +

            "from items as i " +
            "left join bookings as lastBooking on i.item_id = lastBooking.item_id " +
            "left join bookings as nextBooking on i.item_id = nextBooking.item_id " +
            "where (lastBooking.booking_id is null or nextBooking.booking_id is null) and (i.owner_id = :userId) " +
            "or (i.owner_id = :userId and lastBooking.booking_id != nextBooking.booking_id and lastBooking.end_time < nextBooking.start_time and " +
            "nextBooking.start_time > :currentTime) order by i.item_id",
            nativeQuery = true)
    List<ItemLastNextBooking> findAllByUserIdAndTime(@Param("userId") Integer userId,
                                                     @Param("currentTime") LocalDateTime currentTime);


    @Query(value = "SELECT i.item_id as id, i.name as name, i.description as description, i.available as available, " +
            "lastBooking.booking_id as lastBookingId, lastBooking.booker_id as lastBookingBookerId, " +
            "lastBooking.start_time as lastStart, lastBooking.end_time as lastEnd, " +
            "nextBooking.booking_id as nextBookingId, nextBooking.booker_id as nextBookingBookerId, " +
            "nextBooking.start_time as nextStart, nextBooking.end_time as nextEnd, " +
            "i.owner_id as ownerId " +
            "from items as i " +
            "left join bookings as lastBooking on i.item_id = lastBooking.item_id " +
            "left join bookings as nextBooking on i.item_id = nextBooking.item_id " +
            "where (i.item_id = :itemId) and ((lastBooking.booking_id is null or " +
            "nextBooking.booking_id is null) " +
            "or (lastBooking.booking_id != nextBooking.booking_id and lastBooking.end_time <" +
            " nextBooking.start_time and " +
            "nextBooking.start_time > :currentTime)) order by i.item_id limit 1",
            nativeQuery = true)
    ItemLastNextBooking findByItemIdAndTime(@Param("itemId") Integer itemId,
                                            @Param("currentTime") LocalDateTime currentTime);




}
