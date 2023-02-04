package ru.practicum.shareit.user.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

@Repository
@Qualifier("UserRepositoryInDb")
public interface UserRepository extends JpaRepository<User, Integer> {
//    @Query(" select i from Item i " +
//            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
//            " or upper(i.description) like upper(concat('%', ?1, '%'))")
//    List<Item> search(String text);
}
