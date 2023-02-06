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
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    // Booking findFirstBy(int userId);
    List<Booking> findAllByBookerIdOrderByStartDesc(int userId); // ALL

    List<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(int booker_id,
                                                                                 @NotNull LocalDateTime time1,
                                                                                 @NotNull LocalDateTime time2); //
    // CURRENT

    List<Booking> findAllByBookerIdAndEndIsBeforeOrderByStartDesc(int userId, LocalDateTime dateTime); // PAST

    List<Booking> findAllByBookerIdAndStartIsAfterOrderByStartDesc(int userId, LocalDateTime dateTime); // FUTURE

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(int userId, @NotNull BookingStatus status);
    // WAITING or
    // REJECTED


    @Query("select b from Booking b join b.item i where i.owner.id = :userId order by b.start desc")
    List<Booking> findAllByOwnerId(@Param("userId") int userId); // ALL

    @Query("select b from Booking b join b.item i where (i.owner.id = :userId  and b.start < :dateTime1 and b.end > " +
            ":dateTime2) order by b" +
            ".start desc ")
    List<Booking> findAllByOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(@Param("userId") int userId,
                                                                                LocalDateTime dateTime1, LocalDateTime dateTime2); //
    // CURRENT

    @Query("select b from Booking b join b.item i where (i.owner.id = :userId  and b.end < :dateTime) order by b" +
            ".start desc ")
    List<Booking> findAllByOwnerIdAndEndIsBeforeOrderByStartDesc(@Param("userId") int userId,
                                                                 LocalDateTime dateTime); // PAST

    @Query("select b from Booking b join b.item i where (i.owner.id = :userId  and b.start > :dateTime) order by b" +
            ".start desc ")
    List<Booking> findAllByOwnerIdAndStartIsAfterOrderByStartDesc(@Param("userId") int userId,
                                                                  LocalDateTime dateTime); // FUTURE

    @Query("select b from Booking b join b.item i where (i.owner.id = :userId  and b.status = " +
            ":status) order by b.start desc ")
    List<Booking> findAllByOwnerIdAndStatusEqualsOrderByStartDesc(@Param("userId") int userId,
                                                                  @NotNull BookingStatus status); // WAITING or REJECTED

    Optional<Booking> findFirstByBooker_IdAndItem_Id(int booker_id, int item_id);
}