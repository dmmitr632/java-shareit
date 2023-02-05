package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByBookerIdOrderByStartDesc(int userId); // ALL

    List<Booking> findAllByBookerIdAndEndIsAfterOrderByStartDesc(int userId, LocalDateTime dateTime); // CURRENT

    List<Booking> findAllByBookerIdAndEndIsBeforeOrderByStartDesc(int userId, LocalDateTime dateTime); // PAST

    List<Booking> findAllByBookerIdAndStartIsAfterOrderByStartDesc(int userId, LocalDateTime dateTime); // FUTURE

    List<Booking> findAllByBookerIdAndStatusEqualsOrderByStartDesc(int booker_id,
                                                                   @NotNull BookingStatus status); // WAITING or
    // REJECTED


    @Query("select b from Booking b join b.item i where i.owner.id = :userId order by b.start desc")
    List<Booking> findAllByOwnerId(@Param("userId") int userId); // ALL

    @Query("select b from Booking b join b.item i where (i.owner.id = :userId  and b.end > :dateTime) order by b" +
            ".start desc ")
    List<Booking> findAllByOwnerIdAndEndIsAfterOrderByStartDesc(@Param("userId") int userId,
                                                                LocalDateTime dateTime); // CURRENT

    @Query("select b from Booking b join b.item i where (i.owner.id = :userId  and b.end < :dateTime) order by b" +
            ".start desc ")
    List<Booking> findAllByOwnerIdAndEndIsBeforeOrderByStartDesc(@Param("userId") int userId,
                                                                 LocalDateTime dateTime); // PAST

    @Query("select b from Booking b join b.item i where (i.owner.id = :userId  and b.start > :dateTime) order by b" +
            ".start desc ")
    List<Booking> findAllByOwnerIdAndStartIsAfterOrderByStartDesc(@Param("userId") int userId,
                                                                  LocalDateTime dateTime); // FUTURE

    @Query("select b from Booking b join b.item i where (i.owner.id = :userId  and b.status = :status) order by b" +
            ".start desc ")
    List<Booking> findAllByOwnerIdAndStatusEqualsOrderByStartDesc(@Param("userId") int userId,
                                                                  @NotNull BookingStatus status); // WAITING or REJECTED
}