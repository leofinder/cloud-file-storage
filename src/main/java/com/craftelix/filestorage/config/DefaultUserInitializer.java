package com.craftelix.filestorage.config;

import com.craftelix.filestorage.config.properties.DefaultUserProperties;
import com.craftelix.filestorage.entity.Role;
import com.craftelix.filestorage.entity.User;
import com.craftelix.filestorage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class DefaultUserInitializer implements CommandLineRunner {

    private final DefaultUserProperties defaultUserProperties;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername(defaultUserProperties.getName()).isEmpty()) {
            User user = new User();
            user.setUsername(defaultUserProperties.getName());
            user.setPassword(passwordEncoder.encode(defaultUserProperties.getPassword()));
            user.setRoles(Collections.singleton(Role.ROLE_ADMIN));
            userRepository.save(user);
        }
    }
}
