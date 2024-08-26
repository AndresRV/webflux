package com.pragma.powerup.service;

import com.pragma.powerup.entity.Role;
import com.pragma.powerup.exception.CustomException;
import com.pragma.powerup.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    private static final String ROL_NOT_FOUND_MESSAGE = "Rol no encontrado";

    public Flux<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Mono<Role> getRoleById(Long id) {
        return roleRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, ROL_NOT_FOUND_MESSAGE)));
    }

    public Mono<Role> createRole(Role role) {
        return roleRepository.save(role);
    }

    public Mono<Role> updateRole(Long id, Role role) {
        return roleRepository.findById(id)
                .flatMap(existingRole -> {
                    existingRole.setRoleName(role.getRoleName());
                    existingRole.setRoleDescription(role.getRoleDescription());
                    return roleRepository.save(existingRole);
                });
    }

    public Mono<Void> deleteRole(Long id) {
        return roleRepository.deleteById(id);
    }
}
