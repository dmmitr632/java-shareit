package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Page<Booking> findAllByBookerIdOrderByStartDesc(int userId,
                                                    Pageable pageable); // ALL

    Page<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(int bookerId,
                                                                                 @NotNull LocalDateTime time1,
                                                                                 @NotNull LocalDateTime time2,
                                                                                 Pageable pageable); //
    // CURRENT

    Page<Booking> findAllByBookerIdAndEndIsBeforeOrderByStartDesc(int userId, LocalDateTime dateTime,
                                                                  Pageable pageable); // PAST

    Page<Booking> findAllByBookerIdAndStartIsAfterOrderByStartDesc(int userId, LocalDateTime dateTime,
                                                                   Pageable pageable); // FUTURE

    Page<Booking> findAllByBookerIdAndStatusOrderByStartDesc(int userId, @NotNull BookingStatus status,
                                                             Pageable pageable);
    // WAITING or
    // REJECTED


    @Query("select b from Booking b join b.item i where i.owner.id = :userId order by b.start desc")
    Page<Booking> findAllByOwnerId(@Param("userId") int userId,
                                   Pageable pageable); // ALL

    @Query("select b from Booking b join b.item i where (i.owner.id = :userId  and b.start < :dateTime1 and b.end > " +
            ":dateTime2) order by b" +
            ".start desc ")
    Page<Booking> findAllByOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(@Param("userId") int userId,
                                                                                LocalDateTime dateTime1, LocalDateTime dateTime2,
                                                                                Pageable pageable); //
    // CURRENT

    @Query("select b from Booking b join b.item i where (i.owner.id = :userId  and b.end < :dateTime) order by b" +
            ".start desc ")
    Page<Booking> findAllByOwnerIdAndEndIsBeforeOrderByStartDesc(@Param("userId") int userId,
                                                                 LocalDateTime dateTime,
                                                                 Pageable pageable); // PAST

    @Query("select b from Booking b join b.item i where (i.owner.id = :userId  and b.start > :dateTime) order by b" +
            ".start desc ")
    Page<Booking> findAllByOwnerIdAndStartIsAfterOrderByStartDesc(@Param("userId") int userId,
                                                                  LocalDateTime dateTime,
                                                                  Pageable pageable); // FUTURE

    @Query("select b from Booking b join b.item i where (i.owner.id = :userId  and b.status = " +
            ":status) order by b.start desc ")
    Page<Booking> findAllByOwnerIdAndStatusEqualsOrderByStartDesc(@Param("userId") int userId,
                                                                  @NotNull BookingStatus status,
                                                                  Pageable pageable); // WAITING or REJECTED

    Optional<Booking> findFirstByBooker_IdAndItem_Id(int bookerId, int itemId);

}