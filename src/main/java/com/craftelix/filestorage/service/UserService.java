package com.craftelix.filestorage.service;

import com.craftelix.filestorage.dto.user.UserSignupDto;
import com.craftelix.filestorage.entity.Role;
import com.craftelix.filestorage.entity.User;
import com.craftelix.filestorage.exception.UserAlreadyExistException;
import com.craftelix.filestorage.mapper.UserMapper;
import com.craftelix.filestorage.repository.UserRepository;
import com.craftelix.filestorage.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));

        return new CustomUserDetails(user);
    }

    @Transactional
    public void save(UserSignupDto userSignupDto) {
        User user = userMapper.toEntity(userSignupDto);
        user.setPassword(passwordEncoder.encode(userSignupDto.getPassword()));
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            // TODO: добавить обработку исключения ConstraintViolationException
            throw new UserAlreadyExistException(String.format("User %s already exists", userSignupDto.getUsername()), e);
        }
    }
}
