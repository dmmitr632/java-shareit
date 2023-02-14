package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder().id(1).name("user").email("user@user.com").build();
        User user = UserMapper.toUser(userDto);
        userRepository.save(user);
    }

    @Test
    void deleteInBatch() {
        assertThat(userRepository.findAll(), IsCollectionWithSize.hasSize(1));
        userRepository.deleteAll();
        userDto = UserDto.builder().id(1).name("user2").email("user2@user.com").build();
        User user2 = UserMapper.toUser(userDto);
        userRepository.save(user2);
        userDto = UserDto.builder().id(1).name("user3").email("user3@user.com").build();
        User user3 = UserMapper.toUser(userDto);
        userRepository.save(user3);
        assertThat(userRepository.findAll(), IsCollectionWithSize.hasSize(2));
        userRepository.deleteAll();
        assertThat(userRepository.findAll(), IsCollectionWithSize.hasSize(0));
    }
}