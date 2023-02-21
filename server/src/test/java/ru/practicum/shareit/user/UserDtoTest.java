package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoTest {
    @Autowired
    JacksonTester<UserDto> jacksonTester;

    @Test
    void userDto() throws Exception {
        UserDto userDto = UserDto.builder().id(1).name("user1").email("user1@user.com").build();

        JsonContent<UserDto> jsonContent = jacksonTester.write(userDto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("user1");
        assertThat(jsonContent).extractingJsonPathStringValue("$.email").isEqualTo("user1@user.com");
    }
}