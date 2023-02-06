//package ru.practicum.shareit.item.dto;
//
//import lombok.EqualsAndHashCode;
//
//import javax.validation.constraints.NotBlank;
//import java.time.LocalDateTime;
//
//
//public class ItemWithLastNextBookingIds implements ItemLastNextBooking {
//    @EqualsAndHashCode.Exclude
//    Integer id;
//    @NotBlank String name;
//    @NotBlank String description;
//    Boolean available;
//
//    public Integer getId() {
//        return id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public Boolean getAvailable() {
//        return available;
//    }
//
//    public int getLastBookingId() {
//        return lastBookingId;
//    }
//
//    public int getLastBookingBookerId() {
//        return lastBookingBookerId;
//    }
//
//    public LocalDateTime getLastStart() {
//        return lastStart;
//    }
//
//    public LocalDateTime getLastEnd() {
//        return lastEnd;
//    }
//
//    public int getNextBookingId() {
//        return nextBookingId;
//    }
//
//    public int getNextBookingBookerId() {
//        return nextBookingBookerId;
//    }
//
//    public LocalDateTime getNextStart() {
//        return nextStart;
//    }
//
//    public LocalDateTime getNextEnd() {
//        return nextEnd;
//    }
//
//    int lastBookingId;
//    int lastBookingBookerId;
//    LocalDateTime lastStart;
//    LocalDateTime lastEnd;
//
//    int nextBookingId;
//    int nextBookingBookerId;
//    LocalDateTime nextStart;
//    LocalDateTime nextEnd;
//}
