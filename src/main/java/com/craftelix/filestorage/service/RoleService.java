package com.craftelix.filestorage.service;

import com.craftelix.filestorage.entity.Role;
import com.craftelix.filestorage.exception.RoleNotFoundException;
import com.craftelix.filestorage.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException(String.format("Role %s is not found", name)));
    }
}
