package ru.practicum.shareit.request.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "requests", schema = "public")
public class Request {
    @Id
    @EqualsAndHashCode.Exclude
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Integer id;
    @NotBlank
    private String description;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    @NotNull
    private User requester;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NotNull LocalDateTime created;
    @OneToMany(mappedBy = "request")
    @ToString.Exclude
    private Set<Item> items;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Request request = (Request) o;
//        return Objects.equals(id, request.id) && Objects.equals(description, request.description) && Objects.equals(requester, request.requester) && Objects.equals(created, request.created) && Objects.equals(items, request.items);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, description, requester, created, items);
//    }
}
