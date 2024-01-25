package com.example.todoapp.infrastructure.containers;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestKeycloakContainer {

    @Getter
    private final static KeycloakContainer instance = new KeycloakContainer()
                .withRealmImportFile("keycloak/custom-realm.json");

    public static void init() {
        if (!instance.isRunning()) {
            instance.start();
            System.setProperty("KC_URL", instance.getAuthServerUrl() + "/realms/custom-realm");
        }
    }
}
