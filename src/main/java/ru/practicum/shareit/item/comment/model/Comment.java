package ru.practicum.shareit.item.comment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime created;

}
