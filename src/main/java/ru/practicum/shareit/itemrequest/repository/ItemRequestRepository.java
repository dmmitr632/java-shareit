package ru.practicum.shareit.itemrequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.itemrequest.model.ItemRequest;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {

}
