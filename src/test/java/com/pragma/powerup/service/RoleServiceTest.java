package com.pragma.powerup.service;

import com.pragma.powerup.entity.Role;
import com.pragma.powerup.exception.CustomException;
import com.pragma.powerup.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleService roleService;

    @Test
    void getAllRoles() {
        Role role1 = new Role(1L, "Admin", "Admin Role");
        Role role2 = new Role(2L, "User", "User Role");
        when(roleRepository.findAll()).thenReturn(Flux.just(role1, role2));

        StepVerifier.create(roleService.getAllRoles())
                .expectNext(role1)
                .expectNext(role2)
                .verifyComplete();

        verify(roleRepository).findAll();
    }

    @Test
    void getRoleById() {
        Role role = new Role(1L, "Admin", "Admin Role");
        when(roleRepository.findById(1L)).thenReturn(Mono.just(role));

        StepVerifier.create(roleService.getRoleById(1L))
                .expectNext(role)
                .verifyComplete();

        verify(roleRepository).findById(1L);
    }

    @Test
    void getRoleByIdRoleNotFoundException() {
        when(roleRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(roleService.getRoleById(1L))
                .expectErrorSatisfies(throwable -> {
                    assertTrue(throwable instanceof CustomException, "Se esperaba una excepción CustomException");

                    CustomException customException = (CustomException) throwable;
                    assertEquals(HttpStatus.NOT_FOUND, customException.getStatus());

                    assertEquals("Rol no encontrado", customException.getMessage());
                })
                .verify();

        verify(roleRepository).findById(1L);
    }

    @Test
    void createRole() {
        Role role = new Role(null, "Admin", "Admin Role");
        when(roleRepository.save(role)).thenReturn(Mono.just(role));

        StepVerifier.create(roleService.createRole(role))
                .expectNext(role)
                .verifyComplete();

        verify(roleRepository).save(role);
    }

    @Test
    void updateRole() {
        Role existingRole = new Role(1L, "User", "User Role");
        Role updatedRole = new Role(1L, "Admin", "Admin Role");
        when(roleRepository.findById(1L)).thenReturn(Mono.just(existingRole));
        when(roleRepository.save(existingRole)).thenReturn(Mono.just(updatedRole));

        StepVerifier.create(roleService.updateRole(1L, updatedRole))
                .expectNext(updatedRole)
                .verifyComplete();

        verify(roleRepository).findById(1L);
        verify(roleRepository).save(existingRole);
    }

    @Test
    void deleteRole() {
        when(roleRepository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(roleService.deleteRole(1L))
                .verifyComplete();

        verify(roleRepository).deleteById(1L);
    }
}