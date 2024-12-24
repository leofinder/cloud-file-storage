package com.craftelix.filestorage.service;

import com.craftelix.filestorage.dto.user.UserSignupDto;
import com.craftelix.filestorage.entity.User;
import com.craftelix.filestorage.exception.UserAlreadyExistException;
import com.craftelix.filestorage.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = "spring.profiles.active=test")
@Import(TestcontainersConfiguration.class)
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void givenValidUser_whenCreateUser_thenUserIsSaved() {
        UserSignupDto userSignupDto = new UserSignupDto("testUser", "password123", "password123");
        userService.save(userSignupDto);

        Optional<User> savedUser = userRepository.findByUsername(userSignupDto.getUsername());
        assertThat(savedUser)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user.getUsername()).isEqualTo("testUser"));
    }

    @Test
    void givenDuplicateUsername_whenCreateUser_thenThrowsException() {
        UserSignupDto userSignupDto = new UserSignupDto("duplicateUser", "password123", "password123");
        userService.save(userSignupDto);

        UserSignupDto duplicateUserSignupDto = new UserSignupDto("duplicateUser", "password456", "password456");

        assertThatThrownBy(() -> userService.save(duplicateUserSignupDto))
                .isInstanceOf(UserAlreadyExistException.class);
    }

}