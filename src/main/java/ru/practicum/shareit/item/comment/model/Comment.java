package ru.practicum.shareit.item.comment.model;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer id;
    @NotBlank
    String text;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id")
    User author;


}
