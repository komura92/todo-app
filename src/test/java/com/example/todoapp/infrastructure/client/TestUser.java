package com.example.todoapp.infrastructure.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TestUser {
    USER("user", "user"),
    USER_2("user2", "user2");

    private final String username;
    private final String password;
}
