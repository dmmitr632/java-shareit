package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings", schema = "public")
public class Booking {
    @Id
    @EqualsAndHashCode.Exclude
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="booking_id")
    private Integer id;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime start;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime end;
    @ManyToOne
    @JoinColumn(name = "item_id")
    @NotNull
    private Item item;
    @ManyToOne
    @JoinColumn(name = "booker_id")
    @NotNull
    private User booker;
    @NotNull
    @Enumerated
    private BookingStatus status;
}
