package com.example.todoapp.infrastructure.containers;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresContainer  extends PostgreSQLContainer<PostgresContainer> {
    private static final String IMAGE_VERSION = "postgres:16.1-alpine";
    private static PostgresContainer container;

    private PostgresContainer() {
        super(IMAGE_VERSION);
    }

    public static void init() {
        if (container == null) {
            container = new PostgresContainer();
        }
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}
