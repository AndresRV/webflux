package com.pragma.powerup.router;

import com.pragma.powerup.handler.RoleHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RoleRouter {
    private static final String PATH = "rolesFunction";
    private static final String BY_ID = "/{id}";

    @Bean
    RouterFunction<ServerResponse> router(RoleHandler handler) {
        return RouterFunctions.route()
                .GET(PATH, req -> handler.getAllRoles())
                .GET(PATH + BY_ID, handler::getRoleById)
                .POST(PATH, handler::createRole)
                .PUT(PATH + BY_ID, handler::updateRole)
                .DELETE(PATH + BY_ID, handler::deleteRole)
                .build();
    }
}
