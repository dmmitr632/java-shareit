package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestRepository extends PagingAndSortingRepository<Request, Integer> {
    List<Request> findAllByRequesterIdOrderByCreatedDesc(int requesterId);

    Page<Request> findAllByRequesterIdNotOrderByCreatedDesc(int requesterId, Pageable pageable);
}
