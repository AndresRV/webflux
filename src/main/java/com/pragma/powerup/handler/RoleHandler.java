package com.pragma.powerup.handler;

import com.pragma.powerup.entity.Role;
import com.pragma.powerup.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RoleHandler {
    private final RoleService roleService;

    private static final String ID = "id";

    public Mono<ServerResponse> getAllRoles() {
        Flux<Role> roleFlux = roleService.getAllRoles();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(roleFlux, Role.class);
    }

    public Mono<ServerResponse> getRoleById(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        Mono<Role> roleMono = roleService.getRoleById(id);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(roleMono, Role.class);
    }

    public Mono<ServerResponse> createRole(ServerRequest request) {
        Mono<Role> roleMono = request.bodyToMono(Role.class);
        return roleMono.flatMap(role -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(roleService.createRole(role), Role.class));
    }

    public Mono<ServerResponse> updateRole(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable(ID));
        Mono<Role> roleMono = request.bodyToMono(Role.class);
        return roleMono.flatMap(role -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(roleService.updateRole(id, role), Role.class));
    }

    public Mono<ServerResponse> deleteRole(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(roleService.deleteRole(id), Role.class);
    }
}