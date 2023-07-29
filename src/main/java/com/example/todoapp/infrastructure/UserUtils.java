package com.example.todoapp.infrastructure;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.keycloak.KeycloakPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserUtils {

    public static String getActualUserId() {
        return ((KeycloakPrincipal<?>) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getName();
    }
}
