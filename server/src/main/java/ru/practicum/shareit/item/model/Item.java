package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    @EqualsAndHashCode.Exclude
    private Integer id;

    private String name;

    private String description;

    private Boolean available;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "request_id")
    private Request request;

    public Boolean isAvailable() {
        return this.getAvailable();
    }

}